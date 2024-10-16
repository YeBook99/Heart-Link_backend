package com.ss.heartlinkapi.follow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.follow.entity.FollowEntity;

public interface FollowRepository extends JpaRepository<FollowEntity, Long>{
	List<FollowEntity> findByFollower_UserId(Long userId);

}
