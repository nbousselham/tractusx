package com.csds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.csds.entity.DataOfferEntity;

public interface DataOfferMongoRepository extends MongoRepository<DataOfferEntity, String> {

}
