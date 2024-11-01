package com.ss.heartlinkapi.user.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.user.entity.Role;
import com.ss.heartlinkapi.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
	
	/*********** 전화번호로 유저 존재 확인 ***********/
	boolean existsByPhone(String phone);
	
	/*********** 로그인 아이디로 유저 존재 확인 ***********/
	boolean existsByLoginId(String loginId);
	
	/*********** 로그인 아이디로 유저 가져오기 ***********/
	UserEntity findByLoginId(String loginId);
	
	/*********** 전화번호로 유저 가져오기 ***********/
	UserEntity findByPhone(String phone);
	
	// 커플코드로 유저 검색
	UserEntity findByCoupleCode(String code);
	
	/*********** 유저 전체 검색 페이징 처리 ***********/
	Page<UserEntity> findAll(Pageable pageable);
	
	/*********** 유저 아이디 검색 페이징 처리 ***********/
	Page<UserEntity> findByUserId(Long userId, Pageable pageable);
	
	/*********** 유저 로그인 아이디 검색 페이징 처리 ***********/
	Page<UserEntity> findByLoginIdContaining(String loginId, Pageable pageable);
	
	/*********** 유저 가입일자 검색 페이징 처리 ***********/
	Page<UserEntity> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

	/*********** 유저 롤 검색 페이징 처리 ***********/
	Page<UserEntity> findByRole(Role role, Pageable pageable);
	
}
