package com.csds.constants;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csds.entity.ProviderControlPanelConnectorDetails;
import com.csds.repository.ProviderControlPanelConnectorDetailsRepository;

import lombok.Data;

@Data
@Service
public class ApplicationConstantsConfigurationService {

	private ProviderControlPanelConnectorDetails providerControlPanelConnectorDetails;

	@Autowired
	ProviderControlPanelConnectorDetailsRepository providerControlPanelConnectorDetailsRepository;

	@PostConstruct
	public void init() {
		List<ProviderControlPanelConnectorDetails> findAll = providerControlPanelConnectorDetailsRepository.findAll();
		for (ProviderControlPanelConnectorDetails providerControlPanelConnectorDetails : findAll) {
			this.providerControlPanelConnectorDetails = providerControlPanelConnectorDetails;
			break;
		}
	}

	public void readDetails() {
		List<ProviderControlPanelConnectorDetails> findAll = providerControlPanelConnectorDetailsRepository.findAll();
		for (ProviderControlPanelConnectorDetails providerControlPanelConnectorDetails : findAll) {
			this.providerControlPanelConnectorDetails = providerControlPanelConnectorDetails;
			break;
		}
	}

	public ProviderControlPanelConnectorDetails getDatils() {
		return this.providerControlPanelConnectorDetails;
	}
}
