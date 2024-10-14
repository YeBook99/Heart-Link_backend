package com.ss.heartlinkapi.login.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.user.entity.RefreshTokenEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;
import com.ss.heartlinkapi.user.repository.RefreshTokenRepository;
import com.ss.heartlinkapi.user.repository.UserRepository;

@Service
public class RefreshTokenService {
	
	private final RefreshTokenRepository refreshTokenRepository;
	private final UserRepository userRepository;
	
	public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
		this.refreshTokenRepository = refreshTokenRepository;
		this.userRepository = userRepository;
	}

	public void saveRefreshToken(String loginId, String refreshToken, long expirationMillis) {
	    RefreshTokenEntity refreshEntity = new RefreshTokenEntity();
	    UserEntity user = userRepository.findByLoginId(loginId);
	    
	    if (user != null) {
	        refreshEntity.setUser(user);
	        refreshEntity.setRefreshToken(refreshToken);
	        LocalDateTime expiresAt = LocalDateTime.now().plus(expirationMillis, ChronoUnit.MILLIS);
	        refreshEntity.setExpiresAt(expiresAt);
	        
	        refreshTokenRepository.save(refreshEntity);
	    } else {
	        throw new IllegalArgumentException("User not found");
	    }
	}

	public void deleteByRefreshToken(String refreshToken) {
		refreshTokenRepository.deleteByRefreshToken(refreshToken);		
	}
	
}
