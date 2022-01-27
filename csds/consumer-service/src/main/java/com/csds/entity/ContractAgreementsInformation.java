package com.csds.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.csds.model.DataOfferDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "contract_agreement_information")
public class ContractAgreementsInformation {

	private String contractName;
	private String status;
	private String dateEstablished;
	private String dataConsumer;
	private String agreementRemoteId;
	private String dataUrl;
	private Date createdTimeStamp;
	private Date modifiedTimeStamp;

	private DataOfferDetails dataOfferDetails;

}
