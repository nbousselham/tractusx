package com.csds.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csds.valueobjects.UserVO;

@Repository
public interface UserRepository extends CrudRepository<UserVO, Long> {

	@Query("SELECT u FROM UserVO u WHERE u.userName = :userName")
	public UserVO getUserByUserName(@Param("userName") String userName);

	public Optional<UserVO> findById(Long id);
}