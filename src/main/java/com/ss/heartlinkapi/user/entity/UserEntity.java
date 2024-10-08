package com.ss.heartlinkapi.user.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="users")
public class UserEntity {	
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long userId; // 기본키 회원id
    
    @Column(name = "login_id")
	private String loginId; // 일반 로그인 회원 아이디
    
    @Column(unique = true)
    private String email; // 이메일(중복체크)
    
    private String password; // 비밀번호(8~16자이내 특수문자/영어/숫자 모두 포함)
    
    @Column(unique = true, nullable = false)
    private String phone; // 전화번호(문자인증)  
    
    private String name; // 이름 
    
    private boolean gender;	// 성별 남:0, 여:1
    
    private LocalDate birthdate; // 생년월일(6자)  
    
    private String roles; // 역할 여러 개 일시 ','로 구분
    
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt; // 생성일시
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 수정일시
}
