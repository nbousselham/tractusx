package com.csds.service;

import static com.csds.constant.ApplicationMessageConstant.FAILED;
import static com.csds.constant.ApplicationMessageConstant.LOGIN_FAILED;
import static com.csds.constant.ApplicationMessageConstant.LOGIN_INVALID;
import static com.csds.constant.ApplicationMessageConstant.LOGIN_SUCCESS;
import static com.csds.constant.ApplicationMessageConstant.REGISTRATION_ALREADY_FAILED;
import static com.csds.constant.ApplicationMessageConstant.REGISTRATION_FAILED;
import static com.csds.constant.ApplicationMessageConstant.SUCCESS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.csds.repository.UserRepository;
import com.csds.response.ResponseObject;
import com.csds.security.JwtUtil;
import com.csds.valueobjects.UserVO;

@Service
public class AuthService {

	Logger logger = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private JwtUtil jwt;

	@Autowired
	private UserRepository userRepository;

	private String hash(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	private boolean verifyHash(String password, String hash) {
		return BCrypt.checkpw(password, hash);
	}

	public ResponseObject login(UserVO authObj) {
		Assert.notNull(authObj, LOGIN_FAILED);
		UserVO loginVo = userRepository.getUserByUserName(authObj.getUserName());
		ResponseObject response = new ResponseObject();
		boolean isNotValid = true;
		if (loginVo != null) {
			if (verifyHash(authObj.getPassword(), loginVo.getPassword())) {
				String accessToken = jwt.generate(loginVo.getId(), loginVo.getFullName(), "ADMIN",
						loginVo.getEmail(), "ACCESS");

				loginVo.setAccessToken(accessToken);
				response.setStatus(SUCCESS);
				response.setMessage(LOGIN_SUCCESS);
				response.setData(loginVo);

				isNotValid = false;
				logger.info(LOGIN_SUCCESS + " : " + authObj.getUserName());
			}
		}

		if (isNotValid) {
			response.setStatus(FAILED);
			response.setMessage(LOGIN_FAILED);
			logger.info(LOGIN_INVALID + " : " + authObj.getUserName());
		}
		return response;

	}

	public ResponseObject register(UserVO userVO) {
		ResponseObject response = new ResponseObject();
		Assert.notNull(userVO, REGISTRATION_FAILED);
		UserVO userVOfound = userRepository.getUserByUserName(userVO.getUserName());

		if (userVOfound != null) {
			response.setStatus(FAILED);
			response.setMessage(REGISTRATION_ALREADY_FAILED);
			logger.info(REGISTRATION_ALREADY_FAILED);
		} else {
			userVO.setPassword(hash(userVO.getPassword()));
			userRepository.save(userVO);
			response.setStatus(SUCCESS);
			response.setData(userVO);
		}

		return response;

	}

}
