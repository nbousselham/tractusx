package com.csds.api;

import com.csds.model.ContractDescription;
import com.csds.model.ContractResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "DataspaceConnectorContractsApi", url = "placeholder", configuration = DataspaceConnectorConfiguration.class)
public interface DataspaceConnectorContractsApi {
	@PostMapping(path = "/contracts", consumes = MediaType.APPLICATION_JSON_VALUE)
	ContractResponse createContract(URI baseUrl, @RequestHeader("Authorization") String authHeader,
			@RequestBody ContractDescription contractDescription);

	@DeleteMapping(path = "/contracts/{id}")
	void deleteContract(URI baseUrl, @RequestHeader("Authorization") String authHeader, @PathVariable("id") UUID id);

	@PostMapping(path = "/contracts/{id}/rules", consumes = MediaType.APPLICATION_JSON_VALUE)
	void linkRules(URI baseUrl, @RequestHeader("Authorization") String authHeader, @PathVariable("id") UUID id,
			@RequestBody List<String> rules);

	@PostMapping(path = "/contracts/{id}/offers", consumes = MediaType.APPLICATION_JSON_VALUE)
	void linkOffers(URI baseUrl, @RequestHeader("Authorization") String authHeader, @PathVariable("id") UUID id,
			@RequestBody List<String> offers);
}
