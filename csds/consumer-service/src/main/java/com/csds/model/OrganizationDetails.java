package com.csds.model;

import java.util.Set;

import lombok.Data;

@Data
public class OrganizationDetails {

	private Long id;
	private String name;
	private String role;
	private String baseUrl;
	private String status;
	private Set<String> useCase;
}
