package com.csds.controller;

import static org.assertj.core.api.Assertions.assertThat;
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
import com.csds.model.OrganizationDetails;
import com.csds.model.QueryDataOfferModel;
import com.csds.response.ResponseObject;
import com.csds.service.ConsumerControlPanelService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ConsumerControlPanelControllerTest {

	@InjectMocks
	ConsumerControlPanelController consumerControlPanelController;

	@Mock
	private ConsumerControlPanelService consumerControlPanelService;

	@Test
	void testQueryOnDataOffersSuccess() throws Exception {
		
		List<QueryDataOfferModel> qdataOfferList = new ArrayList<>();

		OrganizationDetails consumer = getConsume();
		String recipient = "https://gdsprovider.germanywestcentral.cloudapp.azure.com:8080/api";
		when(consumerControlPanelService.queryOnDataOffers(consumer,recipient,"")).thenReturn(qdataOfferList);

		// when
		ResponseEntity<ResponseObject> result = consumerControlPanelController.queryOnDataOffers(consumer,recipient,"");

		// then
		assertThat(result.getBody().getData()).isEqualTo(qdataOfferList);

	}
	
	@Test
	void testEstablishContractbetweenconsumerAndProviderSuccess() throws Exception {
		
		ContractAgreementsResponse contractAgreementsResponse = new ContractAgreementsResponse();
		String response = "{\"contractName\":\"Contract KAPUTT-FEBRUARY/2022\",\"status\":\"Accepted\",\"dateEstablished\":\"2/2/2022\",\"dataConsumer\":\"KAPUTT\",\"accessLimitedByUsecase\":\"Unlimited\",\"accessLimitedByCompanyRole\":\"Unlimited\",\"usageControl\":\"Unlimited\",\"agreementInformation\":\"https://gdsprovider.germanywestcentral.cloudapp.azure.com:8080/api/agreements/5e043565-c7ec-4749-b0bd-ed239e485a41\",\"dataUrl\":\"https://gdsconsumer.germanywestcentral.cloudapp.azure.com:8080/api/artifacts/4dc95183-aaf0-44ec-9ec3-7bba1cd85659/data/**\",\"createdTimeStamp\":\"2022-02-02T09:43:08.687+00:00\",\"modifiedTimeStamp\":\"2022-02-02T09:43:08.687+00:00\"}";	
		contractAgreementsResponse = new ObjectMapper().readValue(response, ContractAgreementsResponse.class);
		OrganizationDetails consumer = getConsume();
		String recipient = "https://gdsprovider.germanywestcentral.cloudapp.azure.com:8080/api";
		when(consumerControlPanelService.establishContractbetweenconsumerAndProvider(consumer,recipient,"")).thenReturn(contractAgreementsResponse);

		// when
		ResponseEntity<ResponseObject> result = consumerControlPanelController.establishContractbetweenconsumerAndProvider(consumer,recipient,"");

		// then
		assertThat(result.getBody().getData()).isEqualTo(contractAgreementsResponse);
	}
	
	@Test
	void testReadDataOfferSuccess() throws Exception {
		
		OrganizationDetails consumer = getConsume();
		String recipient = "https://gdsprovider.germanywestcentral.cloudapp.azure.com:8080/api";
		when(consumerControlPanelService.readData(consumer,recipient,"")).thenReturn(recipient);
		// when
		ResponseEntity<ResponseObject> result = consumerControlPanelController.readDataOffer(consumer,recipient,"");
		// then
		assertThat(result.getBody().getData()).isEqualTo(recipient);

	}
	
	@Test
	public void testReadContractAgreementsInformationSuccess() throws Exception {
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
	
	private OrganizationDetails getConsume() {
		OrganizationDetails consume = new OrganizationDetails();
		consume.setId(7L);
		consume.setName("KAPUTT");
		consume.setRole("Recycler");
		consume.setBaseUrl("https://gdsconsumer.germanywestcentral.cloudapp.azure.com:8080/api");
		consume.setUsername("admin");
		consume.setPassword("password");
		consume.setStatus("de-active");
		consume.setUseCase(null);
		return consume;
	}
}
