package com.csds.api;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ConsumerArtifactAPI", url = "placeholder", configuration = DataspaceConnectorConfiguration.class)
public interface ConsumerArtifactAPI {

	@GetMapping(path = "/artifacts")
	String artifactDetails(URI baseUrl, @RequestHeader("Authorization") String authHeader);

}
