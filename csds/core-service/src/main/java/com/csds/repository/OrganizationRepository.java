package com.csds.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csds.entities.OrganizationDetails;

public interface OrganizationRepository extends JpaRepository<OrganizationDetails, Long> {

	@Override
	public Optional<OrganizationDetails> findById(Long id);

	public Optional<List<OrganizationDetails>> findByRole(String role);

	@Query(value = "select organization_details.* from organization_details JOIN organization_details_use_case ON id = organization_details_id WHERE use_case =?1", nativeQuery = true)
	public Optional<List<OrganizationDetails>> findByUseCase(String useCase);

}
