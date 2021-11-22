package com.csds.valueobjects;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "users")
public class UserVO {

	@Id
	private Long id;
	
	@Column
	private String fullName;

	@Column
	private String userName;

	@Column
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@Column
	private String email;


	@Transient
	private List<String> roleNames;

	private String accessToken;

}