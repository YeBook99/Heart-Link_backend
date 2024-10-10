package com.ss.heartlinkapi.login.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class JoinDTO {
	
	private String loginId;
	private String name;
	private String password;
	private String email;
	private char gender;
	private String nickname;
	private LocalDate birthDate;
		
}
