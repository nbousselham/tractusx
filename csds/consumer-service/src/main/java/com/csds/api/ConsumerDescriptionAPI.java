package com.csds.api;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ConsumerDescriptionAPI", url = "placeholder", configuration = DataspaceConnectorConfiguration.class)
public interface ConsumerDescriptionAPI {

	@PostMapping(path = "/ids/description")
	String getCatalogDetails(URI baseUrl,
			@RequestHeader("Authorization") String authHeader, @RequestParam("recipient") String recipient,
			@RequestParam("elementId") String elementId);
	
	@PostMapping(path = "/ids/description")
	String getDescriptionDetails(URI baseUrl,
			@RequestHeader("Authorization") String authHeader, @RequestParam("recipient") String recipient,
			@RequestParam("elementId") String elementId);
}
