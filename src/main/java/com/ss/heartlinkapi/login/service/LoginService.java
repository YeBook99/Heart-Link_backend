package com.ss.heartlinkapi.login.service;

import com.ss.heartlinkapi.elasticSearch.service.ElasticService;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.login.dto.JoinDTO;
import com.ss.heartlinkapi.user.entity.ProfileEntity;
import com.ss.heartlinkapi.user.entity.Role;
import com.ss.heartlinkapi.user.entity.UserEntity;
import com.ss.heartlinkapi.user.repository.ProfileRepository;
import com.ss.heartlinkapi.user.repository.UserRepository;

@Service
public class LoginService {
	
	private final UserRepository userRepository;
	private final ProfileRepository profileRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final ElasticService elasticService;

	public LoginService(UserRepository userRepository, ProfileRepository profileRepository,
						BCryptPasswordEncoder passwordEncoder, ElasticService elasticService) {
		this.userRepository = userRepository;
		this.profileRepository = profileRepository;
		this.passwordEncoder = passwordEncoder;
		this.elasticService = elasticService;
	}
	
	/************ 로그인 아이디 중복 확인 ************/
	public boolean checkId(String loginId) {
		return userRepository.existsByLoginId(loginId);
	}
	
	/************ 전화번호로 유저 존재 여부 확인 ************/
	public boolean isUser(String phone) {
		return userRepository.existsByPhone(phone);	
	}
	
	@Transactional
	public boolean saveUser(JoinDTO joinDTO) {
		UserEntity user = new UserEntity();
		user.setLoginId(joinDTO.getLoginId());
		user.setName(joinDTO.getName());	
		String encodedPassword = passwordEncoder.encode(joinDTO.getPassword());
		user.setPassword(encodedPassword);
		user.setEmail(joinDTO.getEmail());
		user.setGender(joinDTO.getGender());
		user.setPhone(joinDTO.getPhone());
		user.setRole(Role.ROLE_USER);
		user.setCoupleCode(CoupleCode.generateRandomCode());
	    try {
	        // save user
	        userRepository.save(user);
			elasticService.addUser(user); // elastic save user
	        ProfileEntity profile = new ProfileEntity();
	        profile.setUserEntity(user);
	        profile.setNickname(joinDTO.getNickname());
	        // save profile
	        return profileRepository.save(profile) != null;
	    } catch (Exception e) {
	        System.err.println("saveUser 실패: " + e.getMessage());
	        return false;
	    }
	}
	
	/************ 로그인 아이디로 유저 찾기 ************/
	public UserEntity findByLoginId(String loginId) {
		return userRepository.findByLoginId(loginId);
	}
	
	/************ 전화번호로 유저 찾기 ************/
	public UserEntity findByPhone(String phone) {
		return userRepository.findByPhone(phone);
	}
	
	/************ 전화번호 업데이트 ************/
	public boolean updatePassword(UserEntity user,String password) {
		String encodedPassword = passwordEncoder.encode(password);
		user.setPassword(encodedPassword);
		return userRepository.save(user) != null;
	}
	
	/************ 비밀번호 일치 확인 ************/
	public boolean checkPassword(UserEntity user, String Password) {
		return passwordEncoder.matches(Password, user.getPassword());
	}
	
}
