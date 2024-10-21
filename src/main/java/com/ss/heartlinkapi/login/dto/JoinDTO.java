package com.ss.heartlinkapi.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinDTO {
	
	private String loginId;
	private String name;
	private String password;
	private String email;
	private char gender;
	private String nickname;
	private String phone;
	private String coupleCode;	
}
