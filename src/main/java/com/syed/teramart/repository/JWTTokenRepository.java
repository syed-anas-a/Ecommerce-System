package com.syed.teramart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.syed.teramart.entity.JWTToken;

@Repository
public interface JWTTokenRepository extends JpaRepository<JWTToken, Integer> {

	@Query("SELECT t FROM JWTToken t WHERE t.user.userId = :userId")
	Optional<JWTToken> findByUserId(@Param("userId") int userId);
	
	@Query("SELECT t FROM JWTToken t where t.token = :token")
	Optional<JWTToken> findByToken(@Param("token") String token);
	
	@Query("DELETE FROM JWTToken t WHERE t.user.userId = :userId")
	void deleteByUser(int userId);
}
