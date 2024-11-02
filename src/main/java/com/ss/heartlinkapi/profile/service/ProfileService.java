package com.ss.heartlinkapi.profile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.couple.service.CoupleService;
import com.ss.heartlinkapi.follow.entity.FollowEntity;
import com.ss.heartlinkapi.follow.repository.FollowRepository;
import com.ss.heartlinkapi.profile.dto.ProfileDTO;
import com.ss.heartlinkapi.user.entity.ProfileEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;
import com.ss.heartlinkapi.user.repository.ProfileRepository;
import com.ss.heartlinkapi.user.repository.UserRepository;

@Service
public class ProfileService {
	
	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;
	private final FollowRepository followRepository;
	private final CoupleService coupleService;
	
	public ProfileService(ProfileRepository profileRepository, UserRepository userRepository,
			FollowRepository followRepository, CoupleService coupleService) {
		this.profileRepository = profileRepository;
		this.userRepository = userRepository;
		this.followRepository = followRepository;
		this.coupleService = coupleService;
	}

	/******* 유저 아이디로 유저 엔티티 가져오는 메서드 *******/
	@Transactional(readOnly = true)
	public UserEntity findByUserId(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}
	
    /******* 유저 프로필 정보 조회 메서드 *******/
	@Transactional(readOnly = true)
    public ProfileDTO getUserProfile(UserEntity userEntity, UserEntity loginUserEntity) {
    	
    	Long userId = userEntity.getUserId();
        ProfileEntity userProfile = selectProfile(userEntity);
        
        int followingCount = followRepository.countFollowingByFollowerId(userId);
        int followersCount = followRepository.countFollowersByUserId(userId);
        
        FollowEntity followEntity = followRepository.findByFollowerAndFollowing(loginUserEntity, userEntity);
        boolean isFollowed = followEntity != null;
        boolean followStatus = (followEntity != null && !followEntity.isStatus());
        
        CoupleEntity couple = coupleService.findByUser1_IdOrUser2_Id(userId);
        Boolean isPrivate = couple.getIsPrivate();
        UserEntity coupleUserEntity = coupleService.getCouplePartner(userId);
        ProfileEntity coupleProfile = selectProfile(coupleUserEntity);
        
        return new ProfileDTO(
            userProfile.getProfile_img(),
            coupleProfile.getProfile_img(),
            userProfile.getBio(),
            userEntity.getLoginId(),
            userProfile.getNickname(),
            followersCount,
            followingCount,
            isFollowed,
            followStatus,
            coupleUserEntity.getUserId(),
            isPrivate
        );
    }
	
	/******* 유저로 프로필 엔티티 가져오는 메서드 *******/
	@Transactional(readOnly = true)
	public ProfileEntity selectProfile(UserEntity userEntity) {
		return profileRepository.findByUserEntity(userEntity);
	}
	
	/******* 유저로 프로필 가져오기 *******/
	@Transactional(readOnly = true)
	public ProfileEntity findByUserEntity(UserEntity user) {
		return profileRepository.findByUserEntity(user);
	}
	
	/******* 애칭 수정하기 *******/
    public void updateNickname(Long userId, String nickName) {
        UserEntity userEntity = findByUserId(userId);
        if (userEntity == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
        }

        ProfileEntity profileEntity = findByUserEntity(userEntity);
        if (profileEntity == null) {
            throw new IllegalArgumentException("프로필이 존재하지 않습니다.");
        }

        if (nickName.length() < 1 || nickName.length() > 10) {
            throw new IllegalArgumentException("닉네임은 1자 이상 10자 이내여야 합니다.");
        }

        profileEntity.setNickname(nickName);
        save(profileEntity);
    }
    
	/******* 상태메세지 수정하기 *******/
    public void updateBio(Long userId, String bio) {
    	
        UserEntity userEntity = findByUserId(userId);
        
        if (userEntity == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
        }

        if (bio.length() > 150) {
            throw new IllegalArgumentException("상태 메시지는 150자 이하로 입력해야 합니다.");
        }

        ProfileEntity profileEntity = findByUserEntity(userEntity);
        if (profileEntity == null) {
            throw new IllegalArgumentException("프로필이 존재하지 않습니다.");
        }

        profileEntity.setBio(bio);
        save(profileEntity);
    }
    
	/******* 프로필 저장 메서드 *******/
	public void save(ProfileEntity profileEntity) {
		profileRepository.save(profileEntity);	
	}
	

}
