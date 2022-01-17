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

import com.csds.constant.ApplicationMessageConstant;
import com.csds.model.UsagePolicies;
import com.csds.model.UsagePolicyType;
import com.csds.response.ResponseObject;
import com.csds.service.PolicesServices;

@ExtendWith(MockitoExtension.class)
class PolicesControllerTest {
	
	
	@InjectMocks
	PolicesController policesController;

	@Mock
	private PolicesServices policesService;

	@Test
	public void testGetAllPolicySuccess() {
		
		// given
		ResponseObject response = new ResponseObject();
		List<UsagePolicies> policyList = getPolicyList();
		response.setData(policyList);
		response.setStatus(ApplicationMessageConstant.SUCCESS);
		response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "getAllPoliceNames"));
		when(policesService.getAllPolicy()).thenReturn(response);

		// when
		ResponseEntity<ResponseObject> result = policesController.getAllPolicy();

		// then
		assertThat(result.getBody().getData()).isEqualTo(policyList);
	}
	
	
	
	
	private List<UsagePolicies> getPolicyList() {

		List<UsagePolicies> usagePoliciesList = new ArrayList<>();

		for(UsagePolicyType uPolicy : UsagePolicyType.values()) {
			if(uPolicy.getLabel().equals(UsagePolicyType.USAGE_LOGGING.getLabel())) {
				break;
			}
			UsagePolicies usagePolicy =  new UsagePolicies();
			usagePolicy.setPolicyType(uPolicy.getLabel());
			usagePolicy.setPolicyDescription(uPolicy.getDescription());
			usagePoliciesList.add(usagePolicy);
		}
		return usagePoliciesList;
	}
}
