package com.csds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.csds.model.ContractAgreementsResponse;
import com.csds.response.ResponseObject;
import com.csds.service.ConsumerControlPanelService;

@ExtendWith(MockitoExtension.class)
class ConsumerControlPanelControllerTest {

	@InjectMocks
	private ConsumerControlPanelController consumerControlPanelController;
	
	@Mock
	private ConsumerControlPanelService consumerControlPanelService;
	
	@Test
	public void testReadContractAgreementsInformation() throws Exception {
		// given
		List<ContractAgreementsResponse> ls = new ArrayList<>();
		generateStub(ls);
		

		when(consumerControlPanelService.readContractAgreementsInformation()).thenReturn(ls);

		// when
		ResponseEntity<ResponseObject> result =consumerControlPanelController.readContractAgreementsInformation();
		// then
		assertEquals(result.getStatusCode(),HttpStatus.OK);
	}
	
	public static void generateStub(final List<ContractAgreementsResponse> ls) {
		
		ContractAgreementsResponse cas = new ContractAgreementsResponse();
		cas.setContractName("Contract Kaputt April2021");
		cas.setStatus("Enable");
		cas.setDateEstablished("5/2/2022");
		cas.setDataConsumer("Kaputt ");
		cas.setAccessLimitedByUsecase("Unlimited");
		cas.setAccessLimitedByCompanyRole("Recycler");
		cas.setUsageControl("Unlimited");
		ls.add(cas);
		
		
	}

}
