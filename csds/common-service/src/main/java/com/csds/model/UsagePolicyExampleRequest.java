package com.csds.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UsagePolicyExampleRequest {

	private String title;
	private String description;
	private String type;
	private Integer value;
	private Date start;
	private Date end;
	private Date deleteDate;
	private String duration;
	private String url;
	private String profile;
	
}
