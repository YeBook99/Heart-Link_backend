package com.ss.heartlinkapi.user.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "refresh_token")
public class RefreshTokenEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long refreshTokenId; // 기본키 리프레쉬 토큰id
	
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "user_id", nullable = false)
	 private UserEntity user; // 회원 테이블 참조
	
	@Column(name = "refresh_token", nullable = false)
	private String refreshToken; // refresh 토큰 값
	
	@CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt; // 생성일시
    
	@Column(name = "expires_at")
	private LocalDateTime expiresAt; // 토큰 만료시간
	
}
