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
	@Transactional(readOnly = true)
	public boolean checkId(String loginId) {
		return userRepository.existsByLoginId(loginId);
	}

	/************ 전화번호로 유저 존재 여부 확인 ************/
	@Transactional(readOnly = true)
	public boolean isUser(String phone) {
		return userRepository.existsByPhone(phone);
	}

	/************ 로그인 검증 ************/
	public void validateJoinDTO(JoinDTO joinDTO) {

		// 이미 있는 회원 400(bad request)
		if (isUser(joinDTO.getPhone())) {
			throw new IllegalArgumentException("이미 존재하는 회원입니다.");
		}

		// 비밀번호 형식 확인
		if (!CheckPassword.isPasswordValid(joinDTO.getPassword())) {
			throw new IllegalArgumentException("비밀번호는 8~16자 이내이며, 특수문자, 영어, 숫자를 모두 포함해야 합니다.");
		}

		// 닉네임 길이 확인
		if (joinDTO.getNickname().length() < 1 || joinDTO.getNickname().length() > 10) {
			throw new IllegalArgumentException("닉네임은 1자 이상 10자 이내여야 합니다.");
		}

		// 아이디 길이 확인
		if (joinDTO.getLoginId().length() < 5 || joinDTO.getLoginId().length() > 15) {
			throw new IllegalArgumentException("아이디는 5자 이상 15자 이내여야 합니다.");
		}

		// 성별 확인
		char gender = joinDTO.getGender();
		if (gender != 'M' && gender != 'F' && gender != 'U') {
			throw new IllegalArgumentException("성별은 'M', 'F', 'U' 중 하나여야 합니다.");
		}

		// 전화번호 확인
		if (!CheckPhone.isPhoneValid(joinDTO.getPhone())) {
			throw new IllegalArgumentException("전화번호 형식이 올바르지 않습니다.");
		}
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
	@Transactional(readOnly = true)
	public UserEntity findByLoginId(String loginId) {
		return userRepository.findByLoginId(loginId);
	}

	/************ 전화번호로 유저 찾기 ************/
	@Transactional(readOnly = true)
	public UserEntity findByPhone(String phone) {
		return userRepository.findByPhone(phone);
	}

	/************ 전화번호 업데이트 ************/
	public boolean updatePassword(UserEntity user, String password) {
		String encodedPassword = passwordEncoder.encode(password);
		user.setPassword(encodedPassword);
		return userRepository.save(user) != null;
	}

	/************ 비밀번호 일치 확인 ************/
	@Transactional(readOnly = true)
	public boolean checkPassword(UserEntity user, String Password) {
		return passwordEncoder.matches(Password, user.getPassword());
	}

}
