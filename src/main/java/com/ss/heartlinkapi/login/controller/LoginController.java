package com.ss.heartlinkapi.login.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.heartlinkapi.login.dto.JoinDTO;
import com.ss.heartlinkapi.login.service.LoginService;

@RestController
@RequestMapping("/user")
public class LoginController {
	
	private final LoginService loginService;
	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}

	@GetMapping("/idcheck")
	public ResponseEntity<?> usercheck(@RequestBody Map<String, String> request) {
		String loginId = request.get("loginId");
		boolean isExist =  loginService.checkId(loginId);
		if(isExist) {
			return ResponseEntity.badRequest().build();
		}else {
			return ResponseEntity.ok().build();
		}
	}

	@PostMapping("/join")
	public ResponseEntity<?> join(@RequestBody JoinDTO joinDTO) {
		//이미 있는 회원 400(bad request)
		boolean isUser = loginService.isUser(joinDTO.getPhone());
		if(isUser) {
			return ResponseEntity.badRequest().build();
		}			
		boolean isJoin=loginService.saveUser(joinDTO);
		if(isJoin) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
