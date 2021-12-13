package com.csds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.csds.entity.ProviderControlPanelConnectorDetails;

public interface ProviderControlPanelConnectorDetailsRepository
		extends MongoRepository<ProviderControlPanelConnectorDetails, String> {

}
