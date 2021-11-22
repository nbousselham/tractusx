package com.csds.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class UsagePolicyDetails {
	
	private String type;
	private Integer value;
	private Date startDate;
	private Date endDate;
	private Date deleteDate;
	private String duration;
	private String url;
}
