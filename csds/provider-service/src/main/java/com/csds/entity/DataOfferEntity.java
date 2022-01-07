package com.csds.entity;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.csds.model.OrganizationDetails;
import com.csds.model.UsagePolicyDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "data_offer")
public class DataOfferEntity {

	private String _id;
	
	private String title;

	private String fileName;
	
	private String fileId;

	private String description;

	private String accessControlUseCaseType;

	private List<String> accessControlUseCase;

	private List<String> byOrganizationRole;

	private List<OrganizationDetails> byOrganization;

	private List<UsagePolicyDetails> usageControl;

	private Integer contractEndsinDays;

	private Map<String,Object> offerIDSdetails;
	
	private String accessControlByRoleType;
	
	private String usageControlType;

}
