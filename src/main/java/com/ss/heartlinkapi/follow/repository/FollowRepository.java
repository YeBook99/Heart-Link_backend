package com.ss.heartlinkapi.follow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.heartlinkapi.follow.entity.FollowEntity;

public interface FollowRepository extends JpaRepository<FollowEntity, Long>{
	
	// 로그인한 회원의 팔로잉 회원 정보
	@Query("SELECT f FROM FollowEntity f JOIN f.follower u WHERE u.loginId = :loginId")
	List<FollowEntity> findFollowingIdsByFollowerLoginId(@Param("loginId") String loginId);

}
