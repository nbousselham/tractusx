package com.csds.api;

import com.csds.model.CatalogDescription;
import com.csds.model.CatalogList;
import com.csds.model.CatalogResponse;
import com.csds.model.GetListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "DataspaceConnectorCatalogsApi", url = "placeholder", configuration = DataspaceConnectorConfiguration.class)
public interface DataspaceConnectorCatalogsApi {
	@PostMapping(path = "/catalogs", consumes = MediaType.APPLICATION_JSON_VALUE)
	CatalogResponse createCatalog(URI baseUrl, @RequestHeader("Authorization") String authHeader,
			@RequestBody CatalogDescription catalogDescription);

	@PostMapping(path = "/catalogs/{catalogId}/offers")
	void linkOffer(URI baseUrl, @RequestHeader("Authorization") String authHeader,
			@PathVariable("catalogId") UUID catalogId, @RequestBody List<String> offers);

	@GetMapping(path = "/catalogs")
	GetListResponse<CatalogList> getAllCatalogs(URI baseUrl, @RequestHeader("Authorization") String authHeader);
}
