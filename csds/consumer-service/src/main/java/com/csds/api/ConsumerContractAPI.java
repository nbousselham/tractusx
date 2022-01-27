package com.csds.api;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ConsumerContractAPI", url = "placeholder", configuration = DataspaceConnectorConfiguration.class)
public interface ConsumerContractAPI {

	@RequestMapping(method = RequestMethod.POST, value = "/ids/contract", consumes = "application/json")
	String negotiateContract(URI baseUrl, @RequestHeader("Authorization") String authHeader, @RequestBody String body,
			@RequestParam("recipient") String recipient, @RequestParam("resourceIds") String offerId,
			@RequestParam("artifactIds") String artifactIds, @RequestParam("download") boolean download);

}
