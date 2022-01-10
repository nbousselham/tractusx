package com.csds.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csds.entities.UseCases;

public interface UseCasesRepository extends JpaRepository<UseCases, Long> {
	
	public Optional<UseCases> findByNameIgnoreCase(String name);
	

}
