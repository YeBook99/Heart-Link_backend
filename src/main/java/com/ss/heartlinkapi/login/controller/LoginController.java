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
import com.ss.heartlinkapi.login.dto.UpdatePasswordDTO;
import com.ss.heartlinkapi.login.service.CheckPassword;
import com.ss.heartlinkapi.login.service.CheckPhone;
import com.ss.heartlinkapi.login.service.LoginService;
import com.ss.heartlinkapi.user.entity.UserEntity;

@RestController
@RequestMapping("/user")
public class LoginController {
	
	private final LoginService loginService;
	
	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}
	
	/********** 아이디 중복 확인 **********/	
	@GetMapping("/idcheck")
	public ResponseEntity<?> usercheck(@RequestBody Map<String, String> request) {
		String loginId = request.get("loginId");
		boolean isExist =  loginService.checkId(loginId);
		if(isExist) {
			return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
		}else {
			return ResponseEntity.ok("사용할 수 있는 아이디입니다.");
		}
	}
	
	/********** 일반 회원가입 **********/
	@PostMapping("/join")
	public ResponseEntity<?> join(@RequestBody JoinDTO joinDTO) {
		
		// 이미 있는 회원 400(bad request)
		boolean isUser = loginService.isUser(joinDTO.getPhone());
		if(isUser) {
			return ResponseEntity.badRequest().build();
		}
		
		// 비밀번호 형식 확인
	    if (!CheckPassword.isPasswordValid(joinDTO.getPassword())) {
	        return ResponseEntity.badRequest().body("비밀번호는 8~16자 이내이며, 특수문자, 영어, 숫자를 모두 포함해야 합니다.");
	    }
	    
	    // 닉네임 길이 확인
	    if (joinDTO.getNickname().length() < 1 || joinDTO.getNickname().length() > 10) {
	        return ResponseEntity.badRequest().body("닉네임은 1자 이상 10자 이내여야 합니다.");
	    }
		
	    // 아이디 길이 확인
	    if (joinDTO.getLoginId().length() < 5 || joinDTO.getLoginId().length() > 15) {
	        return ResponseEntity.badRequest().body("아이디는 5자 이상 15자 이내여야 합니다.");
	    }
	    
	    // 성별 확인
	    char gender = joinDTO.getGender();
	    if (gender != 'M' && gender != 'F' && gender != 'U') {
	        return ResponseEntity.badRequest().body("성별은 'M', 'F', 'U' 중 하나여야 합니다.");
	    }
	    
	    // 전화번호 확인
	    if (!CheckPhone.isPhoneValid(joinDTO.getPhone())) {
	        return ResponseEntity.badRequest().body("전화번호 형식이 올바르지 않습니다.");
	    }
	    
	    // 회원가입
		boolean isJoin=loginService.saveUser(joinDTO);
		if(isJoin) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	/********** 로그인 창에서의 전화번호 변경 **********/
	@PostMapping("/update/password")
	public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
		
		String phone = updatePasswordDTO.getPhone();
		String password = updatePasswordDTO.getPassword();
		String loginId = updatePasswordDTO.getLoginId();
		
	    if (!CheckPassword.isPasswordValid(password)) {
	        return ResponseEntity.badRequest().body("비밀번호는 8~16자 이내이며, 특수문자, 영어, 숫자를 모두 포함해야 합니다.");
	    }
	    
	    // 전화번호 확인
	    if (!CheckPhone.isPhoneValid(phone)) {
	        return ResponseEntity.badRequest().body("전화번호 형식이 올바르지 않습니다.");
	    }
	    
	    // 전화번호로 유저 찾기
	    UserEntity user = loginService.findByPhone(phone);
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저가 존재하지 않습니다.");
	    }

	    // 로그인 아이디로 유저 확인
	    if (!user.getLoginId().equals(loginId)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("로그인 아이디가 일치하지 않습니다.");
	    }
	    
		boolean isUpdated = loginService.updatePassword(user, password);
		if (isUpdated) {
	        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
	    } else {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 실패");
	    }
		
	}
	
	/********** 로그인 창에서의 아이디 찾기 **********/
	@PostMapping("/find/loginId")
	public ResponseEntity<?> findId(@RequestBody Map<String, String> request) {
		
		String phone = request.get("phone");
		
	    // 전화번호 확인
	    if (!CheckPhone.isPhoneValid(phone)) {
	        return ResponseEntity.badRequest().body("전화번호 형식이 올바르지 않습니다.");
	    }
		
	    UserEntity user = loginService.findByPhone(phone);
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저가 존재하지 않습니다.");
	    }
	    // 해당 유저의 로그인 아이디 반환
		return ResponseEntity.ok(user.getLoginId());
	}
}
