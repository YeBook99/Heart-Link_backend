package com.ss.heartlinkapi.follow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.heartlinkapi.follow.entity.FollowEntity;

public interface FollowRepository extends JpaRepository<FollowEntity, Long>{
	
	// 특정 사용자의 팔로잉 회원 정보
	@Query("SELECT f FROM FollowEntity f WHERE f.follower.id =:userId")
	List<FollowEntity> findFollowingIdsByFollowerId(@Param("userId") Long userId);
	
	// 특정 사용자의 팔로잉 회원 수
	@Query("SELECT COUNT(f) FROM FollowEntity f WHERE f.follower.id = :userId")
	int countFollowingIdsByFollowerId(@Param("userId") Long userId);
	
	// 특정 사용자가 팔로우하는 회원 수
	@Query("SELECT COUNT(f) FROM FollowEntity f WHERE f.following.id = :userId")
	int countFollowersByUserId(@Param("userId") Long userId);
	
}
