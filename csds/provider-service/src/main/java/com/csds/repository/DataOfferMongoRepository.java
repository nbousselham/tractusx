package com.csds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.csds.entity.DataOfferEntity;

public interface DataOfferMongoRepository extends MongoRepository<DataOfferEntity, String> {

	@Query(value = "{ 'offerIDSdetails.offerId' : ?0 }")
	DataOfferEntity getofferIDSdetails(String offerId);

}
