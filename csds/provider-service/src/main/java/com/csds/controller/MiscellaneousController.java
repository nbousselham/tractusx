package com.csds.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csds.constant.ApplicationMessageConstant;
import com.csds.constants.ApplicationConstantsConfigurationService;
import com.csds.entity.ProviderControlPanelConnectorDetails;
import com.csds.repository.ProviderControlPanelConnectorDetailsRepository;
import com.csds.response.ResponseObject;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class MiscellaneousController {

	@Autowired
	private ProviderControlPanelConnectorDetailsRepository providerControlPanelConnectorDetailsRepository;

	@Autowired
	private ApplicationConstantsConfigurationService apiConstant;

	@PostMapping("/update-provider-connector-details")
	public ResponseEntity<ResponseObject> updateProviderConnectorDetails(
			@RequestBody ProviderControlPanelConnectorDetails providerControlPanelConnectorDetails) throws Exception {
		log.info("Request recevied : /api/v1/updateProviderConnectorDetails");
		ResponseObject response = null;
		try {
			List<ProviderControlPanelConnectorDetails> list = Optional
					.ofNullable(providerControlPanelConnectorDetailsRepository.findAll()).stream().findFirst().get();
			Object configuration = null;
			if (list.isEmpty()) {
				configuration = providerControlPanelConnectorDetailsRepository
						.save(providerControlPanelConnectorDetails);
			} else {
				ProviderControlPanelConnectorDetails providerControlPanelConnectorDetails2 = list.get(0);
				providerControlPanelConnectorDetails2
						.setConnectorbaseURL(providerControlPanelConnectorDetails.getConnectorbaseURL());
				providerControlPanelConnectorDetails2
						.setConnectorUsername(providerControlPanelConnectorDetails.getConnectorUsername());
				providerControlPanelConnectorDetails2
						.setConnectorPassword(providerControlPanelConnectorDetails.getConnectorPassword());
				configuration = providerControlPanelConnectorDetailsRepository
						.save(providerControlPanelConnectorDetails2);
			}

			apiConstant.readDetails();

			response = new ResponseObject();
			response.setData(configuration);
			response.setStatus(ApplicationMessageConstant.SUCCESS);
			response.setMessage(
					String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "provider connector details update"));
			return ResponseEntity.ok(response);
		} catch (Exception err) {
			log.error("Error in updateProviderConnectorDetails {}", err.toString());
			throw err;
		}
	}

	@GetMapping("/get-provider-connector-details")
	public ResponseEntity<ResponseObject> getProviderConnectorDetails() throws Exception {
		log.info("Request recevied : /api/v1/getProviderConnectorDetails");
		ResponseObject response = null;
		try {
			response = new ResponseObject();
			response.setData(apiConstant.getDatils());
			response.setStatus(ApplicationMessageConstant.SUCCESS);
			response.setMessage(
					String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "get provider connector details"));
			return ResponseEntity.ok(response);
		} catch (Exception err) {
			log.error("Error in getProviderConnectorDetails {}", err.toString());
			throw err;
		}
	}
}
