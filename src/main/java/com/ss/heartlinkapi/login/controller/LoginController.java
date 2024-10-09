package com.ss.heartlinkapi.login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ss.heartlinkapi.login.dto.JoinDTO;

@RestController
public class LoginController {

		@GetMapping("/user")
		public String user() {
			return "확인용 user컨트롤러";
		}
		@PostMapping("/user/join")
		public ResponseEntity<?> join(@RequestBody JoinDTO joinDTO) {
			
			//dto설정 넘어오는정보다받아오기
			//6강 https://www.devyummi.com/page?id=668d525886d3d643f4c18ba0 문서참조회원가입마무리
			return null;
		}
}
