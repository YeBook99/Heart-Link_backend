package com.ss.heartlinkapi.profile.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.heartlinkapi.block.service.BlockService;
import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.couple.service.CoupleService;
import com.ss.heartlinkapi.login.dto.CustomUserDetails;
import com.ss.heartlinkapi.profile.dto.ProfileDTO;
import com.ss.heartlinkapi.profile.service.ProfileService;
import com.ss.heartlinkapi.user.entity.ProfileEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;


@RestController
@RequestMapping("/user")
public class ProfileController {
	
	private final ProfileService profileService;
	private final CoupleService coupleService;
	private final BlockService blockService;

	public ProfileController(ProfileService profileService, CoupleService coupleService, BlockService blockService) {
		this.profileService = profileService;
		this.coupleService = coupleService;
		this.blockService = blockService;
	}

	@GetMapping("/profile/{userId}")
	public ResponseEntity<?> selectProfile(@PathVariable Long userId,@AuthenticationPrincipal CustomUserDetails loginUser){
		
		UserEntity userEntity = profileService.findByUserId(userId);
		if (userEntity == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다.");
	    }

		CoupleEntity loginCouple = coupleService.findCoupleEntity(loginUser.getUserEntity());
		if(blockService.isUserBlockedByCouple(userEntity, loginCouple)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("날 차단한 유저의 프로필");
		}

		CoupleEntity coupleUser = coupleService.findCoupleEntity(userEntity);
		if(blockService.isUserBlockedByCouple(loginUser.getUserEntity(),coupleUser)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("내가 차단한 유저의 프로필");
		}
		
		ProfileEntity userProfile = profileService.selectProfile(userEntity);
		String userimg =userProfile.getProfile_img();
		String bio = userProfile.getBio();
		String loginId = userEntity.getLoginId();
		String nickname =userProfile.getNickname();
		
		int followingCount = profileService.selectFollowingCount(userId);
		int followersCount = profileService.selectFollowersCount(userId);
		UserEntity coupleUserEntity =  coupleService.getCouplePartner(userId);
		Long coupleUserId = coupleUserEntity.getUserId();
		ProfileEntity coupleProfile = profileService.selectProfile(coupleUserEntity);
		String pairimg= coupleProfile.getProfile_img();
		
		ProfileDTO profileDTO = new ProfileDTO(userimg, pairimg, bio, loginId, nickname, followersCount, followingCount, coupleUserId);
		return ResponseEntity.ok(profileDTO);
	}
	
}
