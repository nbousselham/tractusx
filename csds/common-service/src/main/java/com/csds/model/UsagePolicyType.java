package com.csds.model;

import lombok.Getter;

@Getter
public enum UsagePolicyType {

	PROVIDE_ACCESS("PROVIDE_ACCESS", "Provides data usage without any restrictions"), 
	
	PROHIBIT_ACCESS("PROHIBIT_ACCESS","Prohibit data usage"),
	
	N_TIMES_USAGE("N_TIMES_USAGE", "Allows data usage for n times"),	
	
	USAGE_DURING_INTERVAL("USAGE_DURING_INTERVAL", "Provides data usage within a specified time interval"),
	
	DURATION_USAGE("DURATION_USAGE","Allows data usage for a specified time period"),
	
	USAGE_UNTIL_DELETION("USAGE_UNTIL_DELETION","Must delete after usage timeframe"),
	
	USAGE_LOGGING("USAGE_LOGGING","Allows data usage if logged to the Clearing House"),
	
	USAGE_NOTIFICATION("USAGE_NOTIFICATION","Allows data usage with notification message"),
	
	CONNECTOR_RESTRICTED_USAGE("CONNECTOR_RESTRICTED_USAGE","Allows data usage for a specific connector"),
	
	SECURITY_PROFILE_RESTRICTED_USAGE("SECURITY_PROFILE_RESTRICTED_USAGE","Allows data access only for connectors with a specified security level");

	public String label;
	public String description;

	private UsagePolicyType(String label, String description) {
		this.label = label;
		this.description = description;
	}
	
}
