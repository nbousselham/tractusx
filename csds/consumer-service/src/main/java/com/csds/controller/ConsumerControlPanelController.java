package com.csds.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csds.constant.ApplicationMessageConstant;
import com.csds.response.ResponseObject;
import com.csds.service.ConsumerControlPanelService;

@RestController
@RequestMapping("/api/v1")
public class ConsumerControlPanelController {

	private static final Logger logger = LoggerFactory.getLogger(ConsumerControlPanelController.class);

	@Autowired
	private ConsumerControlPanelService consumerControlPanelService;

	@GetMapping(value = "/data-Offers")
	public ResponseEntity<ResponseObject> getAllDataOffers() {
		logger.info("Request recevied : /api/v1/getAllDataOffers");
		ResponseObject response = new ResponseObject();
		response.setStatus(ApplicationMessageConstant.SUCCESS);
		response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "read data offer"));
		return ResponseEntity.ok(response);
	}

}
