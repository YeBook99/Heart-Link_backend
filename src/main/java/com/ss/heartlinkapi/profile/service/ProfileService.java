package com.ss.heartlinkapi.profile.service;

import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.couple.repository.CoupleRepository;
import com.ss.heartlinkapi.follow.repository.FollowRepository;
import com.ss.heartlinkapi.user.entity.ProfileEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;
import com.ss.heartlinkapi.user.repository.ProfileRepository;
import com.ss.heartlinkapi.user.repository.UserRepository;

@Service
public class ProfileService {
	
	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;
	private final FollowRepository followRepository;
	
	public ProfileService(ProfileRepository profileRepository, UserRepository userRepository,
			FollowRepository followRepository) {
		this.profileRepository = profileRepository;
		this.userRepository = userRepository;
		this.followRepository = followRepository;
	}

	// 유저 아이디로 유저 엔티티 가져오는 메서드
	public UserEntity findByUserId(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}
	
	// 유저로 프로필 엔티티 가져오는 메서드
	public ProfileEntity selectProfile(UserEntity userEntity) {
		return profileRepository.findByUserEntity(userEntity);
	}
	// 팔로잉 수 가져오는 메서드
	public int selectFollowingCount(Long userId) {
		return followRepository.countFollowingIdsByFollowerId(userId);
	}
	// 팔로워 수 가져오는 메서드
	public int selectFollowersCount(Long userId) {
		return followRepository.countFollowersByUserId(userId);
	}
	
	// 유저로 프로필 가져오기
	public ProfileEntity findByUserEntity(UserEntity user) {
		return profileRepository.findByUserEntity(user);
	}
	// 프로필 저장 메서드
	public void save(ProfileEntity profileEntity) {
		profileRepository.save(profileEntity);	
	}
	


}
