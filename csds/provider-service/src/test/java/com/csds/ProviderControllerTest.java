package com.csds;

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

import com.csds.controller.ProviderControlPanelController;
import com.csds.entity.DataOfferEntity;
import com.csds.response.ResponseObject;
import com.csds.service.ProviderControlPanelService;

@ExtendWith(MockitoExtension.class)
public class ProviderControllerTest {

	@InjectMocks
	ProviderControlPanelController providerController;

	@Mock
	private ProviderControlPanelService providerControlPanelService;

	@Test
	public void testFindAll() {
		// given
		ResponseObject employee1 = new ResponseObject();

		List<DataOfferEntity> ls = new ArrayList();

		when(providerControlPanelService.getAllDataOffers()).thenReturn(ls);

		// when
		ResponseEntity<ResponseObject> result = providerController.getAllDataOffers();

		// then
		assertThat(result.getBody().getData()).isEqualTo(ls);
	}
}
