package com.csds.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class DataOfferDetails {

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

	private Map<String, Object> offerIDSdetails;

	private String accessControlByRoleType;

	private String usageControlType;
	
	private Date createdTimeStamp;
	
	private Date modifiedTimeStamp;

}
