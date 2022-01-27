package com.csds.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.csds.response.ResponseObject;

@FeignClient(name = "provider-service")
public interface ProviderOfferDetailsAPI {

	@GetMapping(path = "/provider/api/v1/data-Offers/{id}")
	ResponseEntity<ResponseObject> getOfferDetails(@PathVariable String id);
}
