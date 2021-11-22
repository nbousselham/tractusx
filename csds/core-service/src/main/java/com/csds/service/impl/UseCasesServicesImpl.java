package com.csds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csds.constant.ApplicationMessageConstant;
import com.csds.entities.UseCases;
import com.csds.repository.UseCasesRepository;
import com.csds.response.ResponseObject;
import com.csds.service.UseCasesServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UseCasesServicesImpl implements UseCasesServices {

	@Autowired
	private UseCasesRepository useCasesRepository;

	@Override
	public ResponseObject getUseCases() {

		ResponseObject response = new ResponseObject();
		List<UseCases> useCaseList = useCasesRepository.findAll();

		if (useCaseList.size() > 0) {

			response.setData(useCaseList);
			response.setStatus(ApplicationMessageConstant.SUCCESS);
			response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "getUseCases"));

		} else {
			response.setMessage(ApplicationMessageConstant.NOT_FOUND);
			response.setData(useCaseList);
			response.setStatus(ApplicationMessageConstant.SUCCESS);
		}

		log.info(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "getUseCases"));
		return response;
	}

}