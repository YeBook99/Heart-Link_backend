package com.ss.heartlinkapi.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.heartlinkapi.user.entity.SocialEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;

public interface SocialRepository extends JpaRepository<SocialEntity, Long> {
	boolean existsByProviderId(String providerId);

	@Query("SELECT s.userEntity FROM SocialEntity s WHERE s.providerId = :providerId")
	UserEntity findUserByProviderId(@Param("providerId") String providerId);

}
