package com.csds.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.csds.entities.UseCases;
import com.csds.response.ResponseObject;
import com.csds.service.OrganizationServices;
import com.csds.service.UseCasesServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class UseCasesControllerTest {
	
	@InjectMocks
	UseCasesController useCasesController;

	@Mock
	private UseCasesServices useCaseService;
	
	@Mock
	private OrganizationServices orgService;

	@Test
	public void testGetListOfUseCasesSuccess() {
		
		// given
		ResponseObject response = new ResponseObject();
		List<UseCases> useCasesList = getListOfUserCase();
		response.setData(useCasesList);
		when(useCaseService.getUseCases()).thenReturn(response);

		// when
		ResponseEntity<ResponseObject> result = useCasesController.getListOfUseCases();

		// then
		assertThat(result.getBody().getData()).isEqualTo(useCasesList);
	}
	
	
	@Test
	public void testGetOrganizationByUseCaseSuccess() throws JsonMappingException, JsonProcessingException {
		
		// given
		ResponseObject responseObj = new ResponseObject();
		String   response =     "{\"status\":\"SUCCESS\",\"data\":[{\"id\":7,\"name\":\"AUDI\",\"role\":\"Recycler\",\"baseUrl\":\"https://20.79.224.71:8181/api\",\"status\":\"Active\",\"useCase\":[\"Traceability\"]},{\"id\":16,\"name\":\"BMW\",\"role\":\"Recycler\",\"baseUrl\":\"https://20.79.224.71:8181/api\",\"status\":\"Active\",\"useCase\":[\"Traceability\"]},{\"id\":17,\"name\":\"BMW\",\"role\":\"Non Recycler\",\"baseUrl\":\"https://20.79.224.197:8282/api\",\"status\":\"Active\",\"useCase\":[\"Traceability\"]},{\"id\":6,\"name\":\"BOSCH\",\"role\":\"Non Recycler\",\"baseUrl\":\"https://20.79.224.197:8282/api\",\"status\":\"Active\",\"useCase\":[\"Traceability\"]}],\"message\":\"The getAllOrgnizationByUseCase operation successfully completed\",\"page\":null}";
		
		responseObj = new ObjectMapper().readValue(response, ResponseObject.class);
		when(orgService.getAllOrgnizationByUseCase("Traceability")).thenReturn(responseObj);

		// when
		ResponseEntity<ResponseObject> result = useCasesController.getOrganizationByUseCase("Traceability");

		// then
		assertThat(result.getBody().getData()).isEqualTo(responseObj.getData());
	}
	
	
	
	private List<UseCases> getListOfUserCase(){
		List<UseCases> useCasesList = new ArrayList<>();
		UseCases uc = new UseCases();
		uc.setId(1L);
		uc.setName("Circular economy");
		useCasesList.add(uc);
		UseCases uc1 = new UseCases();
		uc1.setId(2L);
		uc1.setName("Sustainability");
		useCasesList.add(uc1);
		UseCases uc2 = new UseCases();
		uc2.setId(3L);
		uc2.setName("Traceability");
		useCasesList.add(uc2);	
		return useCasesList;
	}

	

}
