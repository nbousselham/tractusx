package com.csds.service;


import com.csds.model.request.OrganizationRequest;
import com.csds.response.ResponseObject;

public interface OrganizationServices {

	public ResponseObject getAllRoles();
	public ResponseObject getAllOrgnizationByRole(String role);
	public ResponseObject getAllOrganizations();
	public ResponseObject getAddAndUpdateOrgnizationDetials(OrganizationRequest organizationRequest);
	public ResponseObject getAllOrgnizationByUseCase(String useCase);
	public ResponseObject getDeleteOrgnization(long id);
}
