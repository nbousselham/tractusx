package com.csds.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csds.model.request.OrganizationRequest;
import com.csds.response.ResponseObject;
import com.csds.service.OrganizationServices;

@RestController
@RequestMapping(value = "/api/v1")
public class OrganizationController {

	private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);

	@Autowired
	private OrganizationServices orgService;

	@GetMapping(value = "/roles")
	public ResponseEntity<ResponseObject> getCompanyRoles() {

		logger.info("Started Organization Controller  for : getCompanyRoles");
		return ResponseEntity.ok(orgService.getAllRoles());
	}

	@GetMapping(value = "role/{role}/organizations")
	public ResponseEntity<ResponseObject> getOrganizationByRole(@PathVariable("role") String role) {

		logger.info("Started Organization Controller  for : getCompanies by role");
		return ResponseEntity.ok(orgService.getAllOrgnizationByRole(role));
	}

	@GetMapping(value = "/organizations")
	public ResponseEntity<ResponseObject> getOrganizationDetails() {

		logger.info("Started Organization Controller  for : getOrganizationDetails");
		return ResponseEntity.ok(orgService.getAllOrganizations());
	}

	@PostMapping(value = "/add-update-details")
	public ResponseEntity<ResponseObject> addORUpdateCompany(@RequestBody OrganizationRequest organizationRequest) {

		logger.info("Started Organization Controller  for : addORUpdateCompany");
		return ResponseEntity.ok(orgService.getAddAndUpdateOrgnizationDetials(organizationRequest));
	}

	@DeleteMapping(value = "delete/organizations/{id}")
	public ResponseEntity<ResponseObject> getDeleteOrganizationById(@PathVariable("id") long id) {

		logger.info("Started Organization Controller  for : get Delete Organization By Id");
		return ResponseEntity.ok(orgService.getDeleteOrgnization(id));
	}

}
