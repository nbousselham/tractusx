package com.csds.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class OrganizationRequest {

	private Long id;
	private String name;
	private String role;
	private String baseUrl;
	private String status;


}
