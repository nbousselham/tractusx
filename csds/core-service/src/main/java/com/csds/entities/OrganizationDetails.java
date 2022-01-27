package com.csds.entities;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "organization_details")
public class OrganizationDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;
	private String role;
	private String baseUrl;
	private String username;
	private String password;
	private String status;
	
	@ElementCollection(targetClass = String.class)
	private Set<String> useCase;
}
