package com.csds.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csds.entities.OrganizationDetails;

public interface OrganizationRepository extends JpaRepository<OrganizationDetails, Long> {

	@Override
	public Optional<OrganizationDetails> findById(Long id);

	public Optional<List<OrganizationDetails>> findByRole(String role);

}
