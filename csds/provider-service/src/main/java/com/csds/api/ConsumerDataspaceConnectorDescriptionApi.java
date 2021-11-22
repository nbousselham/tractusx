package com.csds.api;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ConsumerDataspaceConnectorDescriptionApi", url = "placeholder", configuration = DataspaceConnectorConfiguration.class)
public interface ConsumerDataspaceConnectorDescriptionApi {

	@PostMapping(path = "/ids/description", consumes = MediaType.TEXT_PLAIN_VALUE)
	String getAllCatalog(URI baseUrl, @RequestParam("recipient") String recipient,
			@RequestParam("elementId") String elementId);

	@PostMapping(path = "/ids/description", consumes = MediaType.TEXT_PLAIN_VALUE)
	String getAllOffer(URI baseUrl, @RequestParam("recipient") String recipient,
			@RequestParam("elementId") String elementId);

	@PostMapping(path = "/ids/description", consumes = MediaType.TEXT_PLAIN_VALUE)
	String accessData(URI baseUrl, @RequestParam("recipient") String recipient,
			@RequestParam("elementId") String elementId);
}
