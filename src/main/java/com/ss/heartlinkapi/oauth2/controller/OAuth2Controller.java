package com.ss.heartlinkapi.oauth2.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ss.heartlinkapi.oauth2.service.CustomOAuth2UserService;


@RestController
@RequestMapping("/user/auth")
public class OAuth2Controller {
	
	private final CustomOAuth2UserService service;

	public OAuth2Controller(CustomOAuth2UserService service) {
		this.service = service;
	}

	@PostMapping("/phone")
	public ResponseEntity<Void> setPhoneNumber(@RequestBody Map<String, String> request,@RequestParam String providerId){
		String phone = request.get("phone");
		service.setPhone(phone);
		return ResponseEntity.ok().build();
	}
	
}
