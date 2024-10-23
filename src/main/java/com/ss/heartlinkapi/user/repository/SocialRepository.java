package com.ss.heartlinkapi.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.user.entity.SocialEntity;

public interface SocialRepository extends JpaRepository<SocialEntity, Long>{
	boolean existsByProviderId(String providerId);
}
