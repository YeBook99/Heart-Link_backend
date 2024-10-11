package com.ss.heartlinkapi.login.service;

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

	public LoginService(UserRepository userRepository, ProfileRepository profileRepository,
			BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.profileRepository = profileRepository;
		this.passwordEncoder = passwordEncoder;
	}

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
	    try {
	        // save user
	        userRepository.save(user);
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

	public boolean checkId(String loginId) {
		return userRepository.existsByLoginId(loginId);
	}
	
}
