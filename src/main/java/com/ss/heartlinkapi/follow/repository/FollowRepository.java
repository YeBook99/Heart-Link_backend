package com.ss.heartlinkapi.follow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.heartlinkapi.follow.entity.FollowEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;

public interface FollowRepository extends JpaRepository<FollowEntity, Long>{
	
	/******* 유저가 팔로우하고 있는 팔로우엔티티 리스트 반환 ******/
	List<FollowEntity> findByFollowerUserId(Long userId);
	
	/******* 유저를 팔로우하고 있는 팔로우엔티티 리스트 반환 ******/
    List<FollowEntity> findByFollowingUserId(Long userId);
	
	/******* 팔로워유저와 팔로잉유저로 팔로우 객체 반환 ******/
    FollowEntity findByFollowerAndFollowing(UserEntity follower, UserEntity following);
    
	// 특정 사용자의 팔로잉 회원 수
	@Query("SELECT COUNT(f) FROM FollowEntity f WHERE f.follower.id = :userId")
	int countFollowingByFollowerId(@Param("userId") Long userId);
	
	// 특정 사용자가 팔로우하는 회원 수
	@Query("SELECT COUNT(f) FROM FollowEntity f WHERE f.following.id = :userId")
	int countFollowersByUserId(@Param("userId") Long userId);
	
}
