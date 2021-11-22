package com.csds.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csds.response.ResponseObject;
import com.csds.service.PolicesServices;

@RestController
@RequestMapping(value = "/api/v1")
public class PolicesController {

	private static final Logger logger = LoggerFactory.getLogger(PolicesController.class);

	@Autowired
	private PolicesServices policesService;

	@GetMapping(value = "/polices")
	public ResponseEntity<ResponseObject> getAllPolicy() {

		logger.info("Started Polices Controller  for : getAllPolicy");
		return ResponseEntity.ok(policesService.getAllPolicy());
	}

}
