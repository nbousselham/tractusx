package com.csds.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csds.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	public Optional<Role> findByRoleIgnoreCase(String role);

}
