package com.csds.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.csds.constant.ApplicationMessageConstant;
import com.csds.entities.OrganizationDetails;
import com.csds.entities.Role;
import com.csds.entities.UseCases;
import com.csds.model.request.OrganizationRequest;
import com.csds.repository.OrganizationRepository;
import com.csds.repository.RoleRepository;
import com.csds.repository.UseCasesRepository;
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
	
	@Autowired
	private UseCasesRepository useCasesRepository;
	
	

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
	public ResponseObject getAllOrganizations() {
		ResponseObject response = new ResponseObject();
		List<OrganizationDetails> orgList = organizationRepository.findAll();

		if (orgList != null) {
			response.setData(orgList);
			response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "getAllOrganizations"));
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
		ResponseObject response = new ResponseObject();
		
		if(validateOrganizationExitOrNot(organizationRequest)) {
			
			response.setMessage(ApplicationMessageConstant.ORGANIZATION_VALIDATION_FAILD);
			response.setData(null);
			response.setStatus(ApplicationMessageConstant.SUCCESS);
			log.info("getAddAndUpdateOrgnizationDetials : Organization Already Exist in Database : {}",organizationRequest.getName());
			return response;
		}

		Optional<OrganizationDetails> optionalOrgDetails = organizationRepository.findById(organizationRequest.getId());
		OrganizationDetails organizationDetails;
	
		if (optionalOrgDetails.isPresent()) {
			organizationDetails = optionalOrgDetails.get();
		} else {
			organizationDetails = new OrganizationDetails();
		}
		
		organizationDetails.setName(organizationRequest.getName());
		if (StringUtils.isNotBlank(organizationRequest.getRole())) {
			saveRoleInDBIfNotAvaiable(organizationRequest.getRole());
		}
		organizationDetails.setRole(organizationRequest.getRole());

		if (!organizationRequest.getUseCase().isEmpty()) {
			saveUseCaseInDBIfNotAvaiable(organizationRequest.getUseCase());
		}
		organizationDetails.setUseCase(organizationRequest.getUseCase());
		organizationDetails.setBaseUrl(organizationRequest.getBaseUrl());
		organizationDetails.setUsername(organizationRequest.getUsername());
		organizationDetails.setPassword(organizationRequest.getPassword());
		organizationDetails.setStatus(organizationRequest.getStatus());
		organizationRepository.saveAndFlush(organizationDetails);

		response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "add update organization"));
		response.setData(organizationDetails);
		response.setStatus(ApplicationMessageConstant.SUCCESS);
		log.info(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "getAddAndUpdateOrgnizationDetials"));
		return response;
	}


	@Override
	public ResponseObject getAllOrgnizationByUseCase(String useCase) {
		Assert.notNull(useCase, ApplicationMessageConstant.VALIDATION_FAILED);
		ResponseObject response = new ResponseObject();
		Optional<List<OrganizationDetails>> optionalOrgList = organizationRepository.findByUseCase(useCase);

		if (optionalOrgList.isPresent()) {
			response.setData(optionalOrgList.get());
			response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "getAllOrgnizationByUseCase"));
			response.setStatus(ApplicationMessageConstant.SUCCESS);
		} else {
			response.setMessage(ApplicationMessageConstant.NOT_FOUND);
			response.setData(null);
			response.setStatus(ApplicationMessageConstant.SUCCESS);
		}
		log.info(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "getAllOrgnizationByUseCase"));
		return response;
	}
	
	
	@Override
	public ResponseObject getDeleteOrgnization(long id) {
		
		ResponseObject response = new ResponseObject();
		
		if(id<0) {
			response.setMessage("ID should not be null or empty");
			response.setData(null);
			response.setStatus(ApplicationMessageConstant.SUCCESS);
		
		}else {
			organizationRepository.deleteById(id);
			response.setData(null);
			response.setMessage( id +" : ID deleted successfully from database");
			response.setStatus(ApplicationMessageConstant.SUCCESS);
			log.info("getDeleteOrgnization : Successfully deleted provided ID : {}",id);
		}
		return response;
	}
	
	private boolean validateOrganizationExitOrNot(OrganizationRequest organizationRequest) {

		boolean flag = false;
		Optional<OrganizationDetails> orgList = organizationRepository
				.findByNameIgnoreCase(organizationRequest.getName());
		if (orgList.isPresent() && organizationRequest.getId() == 0) {
			flag = true;
		}
		return flag;
	}
	
	
	private void saveUseCaseInDBIfNotAvaiable(Set<String> useCaseList) {

		Set<UseCases> useCasesListDb = new HashSet<>();

		for (String useCase : useCaseList) {
			Optional<UseCases> useCaseOptional = useCasesRepository.findByNameIgnoreCase(useCase);

			if (!useCaseOptional.isPresent() || StringUtils.isBlank(useCaseOptional.get().getName())) {
				UseCases useCaseDb = new UseCases();
				useCaseDb.setName(useCase);
				useCasesListDb.add(useCaseDb);
			}
		}
		useCasesRepository.saveAllAndFlush(useCasesListDb);
	}

	private void saveRoleInDBIfNotAvaiable(String role) {
		
		Optional<Role> roleOptional = roleRepository.findByRoleIgnoreCase(role);
		if (!roleOptional.isPresent() || StringUtils.isBlank(roleOptional.get().getRole())) {
			Role roleDb = new Role();
			roleDb.setRole(role);
			roleRepository.saveAndFlush(roleDb);
		}	
	}
}
