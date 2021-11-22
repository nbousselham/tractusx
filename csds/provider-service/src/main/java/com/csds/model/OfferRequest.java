package com.csds.model;

import java.util.List;

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
public class OfferRequest {

	private String id;
	private String title;
	private String description;
	private String accessControlUseCaseType;
	private List<String> accessControlUseCase;
	private List<String> byOrganizationRole;
	private List<OrganizationDetails> byOrganization;
	private List<UsagePolicyDetails> usageControl;
	private Integer contractEndsinDays;

}
