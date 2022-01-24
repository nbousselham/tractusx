package com.csds.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractAgreementsResponse {
	
	private String contractName;
	private String status;
	private String dateEstablished;
	private String dataConsumer;
	private String accessLimitedByUsecase;
	private String accessLimitedByCompanyRole;
	private String usageControl;

}
