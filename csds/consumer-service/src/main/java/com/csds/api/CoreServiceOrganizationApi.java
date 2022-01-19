package com.csds.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.csds.response.ResponseObject;

@FeignClient(name = "core-service")
public interface CoreServiceOrganizationApi {

	@GetMapping(path = "/core/api/v1/organizations")
	ResponseEntity<ResponseObject> getAllOrganizations();
}
