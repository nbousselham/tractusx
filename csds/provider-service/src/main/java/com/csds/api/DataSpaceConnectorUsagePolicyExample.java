package com.csds.api;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.csds.model.UsagePolicyExampleRequest;

@FeignClient(name = "DataSpaceConnectorUsagePolicyExample", url = "placeholder", configuration = DataspaceConnectorConfiguration.class)
public interface DataSpaceConnectorUsagePolicyExample {

	@PostMapping(path = "/examples/policy", consumes = MediaType.APPLICATION_JSON_VALUE)
	String getUsagePolicyExample(URI baseUrl, @RequestHeader("Authorization") String authHeader,
			@RequestBody UsagePolicyExampleRequest requestBody);
}
