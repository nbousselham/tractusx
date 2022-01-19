package com.csds.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.csds.response.ResponseObject;
import com.csds.service.OrganizationServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class OrganizationControllerTest {
	
	
	@InjectMocks
	OrganizationController organizationController;

	@Mock
	private OrganizationServices orgService;

	@Test
	public void testGetCompanyRolesSuccess() throws JsonMappingException, JsonProcessingException {
		
		ResponseObject responseObj = new ResponseObject();
		String   response = "{\"status\":\"SUCCESS\",\"data\":[{\"id\":1,\"role\":\"Recycler\"},{\"id\":2,\"role\":\"Non Recycler\"},{\"id\":3,\"role\":\"OEM\"}],\"message\":\"The getAllRoles operation successfully completed\",\"page\":null}";	
		responseObj = new ObjectMapper().readValue(response, ResponseObject.class);
		when(orgService.getAllRoles()).thenReturn(responseObj);

		// when
		ResponseEntity<ResponseObject> result = organizationController.getCompanyRoles();

		// then
		assertThat(result.getBody().getData()).isEqualTo(responseObj.getData());

	}
	
	@Test
	public void testGetOrganizationDetailsSuccess() throws JsonMappingException, JsonProcessingException {
		
		ResponseObject responseObj = new ResponseObject();
		String   response = "{\"status\":\"SUCCESS\",\"data\":[{\"id\":7,\"name\":\"AUDI\",\"role\":\"Recycler\",\"baseUrl\":\"https://20.79.224.71:8181/api\",\"status\":\"Active\",\"useCase\":[\"Traceability\"]},{\"id\":16,\"name\":\"BMW\",\"role\":\"Recycler\",\"baseUrl\":\"https://20.79.224.71:8181/api\",\"status\":\"Active\",\"useCase\":[\"Traceability\"]},{\"id\":17,\"name\":\"BMW\",\"role\":\"Non Recycler\",\"baseUrl\":\"https://20.79.224.197:8282/api\",\"status\":\"Active\",\"useCase\":[\"Traceability\"]},{\"id\":6,\"name\":\"BOSCH\",\"role\":\"Recycler\",\"baseUrl\":\"https://20.79.224.197:8282/api\",\"status\":\"Active\",\"useCase\":[\"Traceability\"]}],\"message\":\"The getAllOrganizations operation successfully completed\",\"page\":null}";	
		responseObj = new ObjectMapper().readValue(response, ResponseObject.class);
		when(orgService.getAllOrganizations()).thenReturn(responseObj);

		// when
		ResponseEntity<ResponseObject> result = organizationController.getOrganizationDetails();

		// then
		assertThat(result.getBody().getData()).isEqualTo(responseObj.getData());

	}
	
	@Test
	public void testGetOrganizationByRoleSuccess() throws JsonMappingException, JsonProcessingException {
		
		ResponseObject responseObj = new ResponseObject();
		String   response = "{\"status\":\"SUCCESS\",\"data\":[{\"id\":7,\"name\":\"AUDI\",\"role\":\"Recycler\",\"baseUrl\":\"https://20.79.224.71:8181/api\",\"status\":\"Active\",\"useCase\":[\"Traceability\"]},{\"id\":16,\"name\":\"BMW\",\"role\":\"Recycler\",\"baseUrl\":\"https://20.79.224.71:8181/api\",\"status\":\"Active\",\"useCase\":[\"Traceability\"]},{\"id\":6,\"name\":\"BOSCH\",\"role\":\"Recycler\",\"baseUrl\":\"https://20.79.224.197:8282/api\",\"status\":\"Active\",\"useCase\":[\"Traceability\"]}],\"message\":\"The getAllOrgnizationByRole operation successfully completed\",\"page\":null}";	
		responseObj = new ObjectMapper().readValue(response, ResponseObject.class);
		when(orgService.getAllOrgnizationByRole("Recycler")).thenReturn(responseObj);

		// when
		ResponseEntity<ResponseObject> result = organizationController.getOrganizationByRole("Recycler");

		// then
		assertThat(result.getBody().getData()).isEqualTo(responseObj.getData());

	}

}
