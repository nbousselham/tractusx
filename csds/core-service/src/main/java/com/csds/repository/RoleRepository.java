package com.csds.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csds.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
