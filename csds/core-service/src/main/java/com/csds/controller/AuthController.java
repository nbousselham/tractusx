package com.csds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csds.response.ResponseObject;
import com.csds.service.AuthService;
import com.csds.valueobjects.UserVO;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping(value = "/login")
	public ResponseEntity<ResponseObject> login(@RequestBody UserVO authRequest) {
		return ResponseEntity.ok(authService.login(authRequest));
	}

	@PostMapping(value = "/register")
	public ResponseEntity<ResponseObject> register(@RequestBody UserVO authRequest) {
		return ResponseEntity.ok(authService.register(authRequest));
	}

	@GetMapping(value = "/index")
	public String getMethodName() {
		return "Welcome to core service";
	}

}