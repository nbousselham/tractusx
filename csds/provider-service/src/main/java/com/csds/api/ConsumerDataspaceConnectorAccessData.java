package com.csds.api;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;

@FeignClient(name = "ConsumerDataspaceConnectorAccessData", url = "placeholder", configuration = DataspaceConnectorConfiguration.class)
public interface ConsumerDataspaceConnectorAccessData {

	@GetMapping(path = "/artifacts")
	JsonNode getArtifactDetailsBasedonAgreement(URI baseUrl);

	@GetMapping(path = "/**", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	Object readData(URI baseUrl, @RequestParam("download") boolean download,
			@RequestParam("argreementUri") String argreementUri);

}
