package com.csds.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csds.constant.ApplicationMessageConstant;
import com.csds.model.OrganizationDetails;
import com.csds.response.ResponseObject;
import com.csds.service.ConsumerControlPanelService;

@RestController
@RequestMapping("/api/v1")
public class ConsumerControlPanelController {

	private static final Logger logger = LoggerFactory.getLogger(ConsumerControlPanelController.class);

	@Autowired
	private ConsumerControlPanelService consumerControlPanelService;

	@PostMapping(value = "/query-data-Offers")
	public ResponseEntity<ResponseObject> queryOnDataOffers(@RequestBody OrganizationDetails consumer,
			@RequestParam("recipient") String recipient, @RequestParam("elementId") String elementId) throws Exception {
		
		logger.info("Request recevied : /api/v1/query-data-Offers");
		ResponseObject response = new ResponseObject();
		response.setData(consumerControlPanelService.queryOnDataOffers(consumer, recipient, elementId));
		response.setStatus(ApplicationMessageConstant.SUCCESS);
		response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "query on data offer"));
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/establish-data-Offers-contract")
	public ResponseEntity<ResponseObject> establishContractbetweenconsumerAndProvider(
			@RequestBody OrganizationDetails consumer, @RequestParam("recipient") String recipient,
			@RequestParam("elementId") String elementId) {
		
		logger.info("Request recevied : /api/v1/establish-data-Offers-contract");
		ResponseObject response = new ResponseObject();
		response.setData(consumerControlPanelService.establishContractbetweenconsumerAndProvider(consumer, recipient,
				elementId));
		response.setStatus(ApplicationMessageConstant.SUCCESS);
		response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "establish contract"));
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/read-data-Offers")
	public ResponseEntity<ResponseObject> readDataOffer(@RequestBody OrganizationDetails consumer,
			@RequestParam("recipient") String recipient, @RequestParam("elementId") String elementId)
			throws FileNotFoundException, IOException, Exception {
		
		logger.info("Request recevied : /api/v1/read-data-Offers");
		ResponseObject response = new ResponseObject();
		response.setData(consumerControlPanelService.readData(consumer, recipient, elementId));
		response.setStatus(ApplicationMessageConstant.SUCCESS);
		response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "read data offer"));
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/read-contract-agreements")
	public ResponseEntity<ResponseObject> readContractAgreementsInformation() throws Exception {
		
		logger.info("Request recevied : /api/v1/readContractAgreementsInformation");
		ResponseObject response = new ResponseObject();
		response.setData(consumerControlPanelService.readContractAgreementsInformation());
		response.setStatus(ApplicationMessageConstant.SUCCESS);
		response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "read contract information"));
		return ResponseEntity.ok(response);
	}

}
