package com.csds.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.csds.constant.ApplicationMessageConstant;
import com.csds.entities.OrganizationDetails;
import com.csds.entities.Role;
import com.csds.model.request.OrganizationRequest;
import com.csds.repository.OrganizationRepository;
import com.csds.repository.RoleRepository;
import com.csds.response.ResponseObject;
import com.csds.service.OrganizationServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrganizationServicesImpl implements OrganizationServices {

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public ResponseObject getAllRoles() {
		ResponseObject response = new ResponseObject();
		List<Role> orgList = roleRepository.findAll();

		if (orgList != null) {
			response.setData(orgList);
			response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "getAllRoles"));
			response.setStatus(ApplicationMessageConstant.SUCCESS);
		} else {
			response.setMessage(ApplicationMessageConstant.NOT_FOUND);
			response.setData(null);
			response.setStatus(ApplicationMessageConstant.SUCCESS);
		}
		log.info(ApplicationMessageConstant.SUCCESS_OPERATION, "getAllRoles");
		return response;
	}

	@Override
	public ResponseObject getAllOrgnizationByRole(String role) {

		Assert.notNull(role, ApplicationMessageConstant.VALIDATION_FAILED);
		ResponseObject response = new ResponseObject();
		Optional<List<OrganizationDetails>> optionalOrgList = organizationRepository.findByRole(role);

		if (optionalOrgList.isPresent()) {
			response.setData(optionalOrgList.get());
			response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "getAllOrgnizationByRole"));
			response.setStatus(ApplicationMessageConstant.SUCCESS);
		} else {
			response.setMessage(ApplicationMessageConstant.NOT_FOUND);
			response.setData(null);
			response.setStatus(ApplicationMessageConstant.SUCCESS);
		}
		log.info(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "getAllOrgnizationByRole"));
		return response;
	}

	@Override
	public ResponseObject getAllComapnies() {
		ResponseObject response = new ResponseObject();
		List<OrganizationDetails> orgList = organizationRepository.findAll();

		if (orgList != null) {
			response.setData(orgList);
			response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "getAllComapnies"));
			response.setStatus(ApplicationMessageConstant.SUCCESS);
		} else {
			response.setMessage(ApplicationMessageConstant.NOT_FOUND);
			response.setData(null);
			response.setStatus(ApplicationMessageConstant.SUCCESS);
		}
		log.info(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "getAllComapnies"));
		return response;
	}

	@Override
	public ResponseObject getAddAndUpdateOrgnizationDetials(OrganizationRequest organizationRequest) {

		Assert.notNull(organizationRequest, ApplicationMessageConstant.VALIDATION_FAILED);

		Optional<OrganizationDetails> optionalOrgDetails = organizationRepository.findById(organizationRequest.getId());
		OrganizationDetails organizationDetails;
		ResponseObject response = new ResponseObject();
		if (optionalOrgDetails.isPresent()) {
			organizationDetails = optionalOrgDetails.get();
		} else {
			organizationDetails = new OrganizationDetails();
		}

		organizationDetails.setName(organizationRequest.getName());
		organizationDetails.setRole(organizationRequest.getRole());
		organizationDetails.setBaseUrl(organizationRequest.getBaseUrl());
		organizationDetails.setStatus(organizationRequest.getStatus());
		organizationRepository.saveAndFlush(organizationDetails);

		response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "add update organization"));
		response.setData(organizationDetails);
		response.setStatus(ApplicationMessageConstant.SUCCESS);
		log.info(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "getAddAndUpdateOrgnizationDetials"));
		return response;
	}
}
