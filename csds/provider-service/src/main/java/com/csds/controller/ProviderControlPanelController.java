package com.csds.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.csds.constant.ApplicationMessageConstant;
import com.csds.entity.DataOfferEntity;
import com.csds.exception.ServiceException;
import com.csds.exception.ValidationException;
import com.csds.model.OfferFile;
import com.csds.model.OfferRequest;
import com.csds.response.ResponseObject;
import com.csds.service.ProviderControlPanelService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1")
public class ProviderControlPanelController {

	private static final Logger logger = LoggerFactory.getLogger(ProviderControlPanelController.class);

	@Autowired
	private ProviderControlPanelService providerControlPanelService;

	@GetMapping(value = "/data-Offers")
	public ResponseEntity<ResponseObject> getAllDataOffers() {
		logger.info("Request recevied : /api/v1/getAllDataOffers");
		List<DataOfferEntity> allDataOffers = providerControlPanelService.getAllDataOffers();
		ResponseObject response = new ResponseObject();
		response.setData(allDataOffers);
		response.setStatus(ApplicationMessageConstant.SUCCESS);
		response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "read data offer"));
		return ResponseEntity.ok(response);
	}

	@RequestMapping(path = "/create-data-offer", method = RequestMethod.POST, consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<ResponseObject> createDataOffer(@RequestPart("offerRequest") String offerRequest,
			@RequestPart("file") MultipartFile file, HttpServletRequest httpServletResponse) throws Exception {
		logger.info("Request recevied : /api/v1/createDataOffer");
		OfferRequest offerRequestObj = new OfferRequest();
		ResponseObject response = null;
		try {

			ObjectMapper objectMapper = new ObjectMapper();
			offerRequestObj = objectMapper.readValue(offerRequest, OfferRequest.class);
			Object createDataOffer = providerControlPanelService.createDataOffer(offerRequestObj, file);
			response = new ResponseObject();
			response.setData(createDataOffer);
			response.setStatus(ApplicationMessageConstant.SUCCESS);
			response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "create data offer"));
			return ResponseEntity.ok(response);

		} catch (ValidationException | ServiceException err) {
			logger.error("Error in createDataOffer {}", err.toString());
			throw err;
		}
	}

	@RequestMapping(path = "/update-data-offer", method = RequestMethod.POST, consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<ResponseObject> updateDataOffer(@RequestPart("offerRequest") String offerRequest,
			@RequestPart("file") MultipartFile file, HttpServletRequest httpServletResponse) throws Exception {
		logger.info("Request recevied : /api/v1/updateDataOffer");
		OfferRequest offerRequestObj = new OfferRequest();
		ResponseObject response = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			offerRequestObj = objectMapper.readValue(offerRequest, OfferRequest.class);
			Object createDataOffer = providerControlPanelService.updateDataOffer(offerRequestObj, file);
			response = new ResponseObject();
			response.setData(createDataOffer);
			response.setStatus(ApplicationMessageConstant.SUCCESS);
			response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "update data offer"));
			return ResponseEntity.ok(response);
		} catch (IOException | ServiceException err) {
			logger.error("Error in updateDataOffer {}", err.toString());
			throw err;
		}
	}

	@GetMapping("/stream-data-offer-file/{id}")
	public void streamFile(@PathVariable String id, HttpServletResponse response) throws Exception {
		try {
			logger.info("Request recevied : /api/v1/streamFile");
			OfferFile offerFile = providerControlPanelService.getOfferFile(id);
			FileCopyUtils.copy(offerFile.getStream(), response.getOutputStream());
		} catch (Exception err) {
			logger.error("Error {}", err.toString());
		}
	}
}
