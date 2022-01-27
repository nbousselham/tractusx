package com.csds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.csds.entity.ContractAgreementsInformation;

public interface ContractAgreementsInformationMongoRepository extends MongoRepository<ContractAgreementsInformation, String> {

}
