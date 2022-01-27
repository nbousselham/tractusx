package com.csds.model;

import java.util.List;
import java.util.Map;

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
public class QueryDataOfferModel {

	private String id;

	private String offerInfo;

	private String contractInfo;
	
	private List<Map<String,String>> contractRules;
	
	private String representation;
	
	private String artifact;
}
