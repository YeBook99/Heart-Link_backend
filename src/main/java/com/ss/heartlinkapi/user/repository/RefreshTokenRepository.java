package com.ss.heartlinkapi.user.repository;


import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.user.entity.RefreshTokenEntity;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long>{
	
	Boolean existsByRefreshToken(String refreshToken);
	
    @Transactional
    void deleteByRefreshToken(String refreshToken);

}
