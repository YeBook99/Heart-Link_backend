package com.ss.heartlinkapi.profile.controller;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ss.heartlinkapi.couple.service.CoupleService;
import com.ss.heartlinkapi.login.dto.CustomUserDetails;
import com.ss.heartlinkapi.login.service.LoginService;
import com.ss.heartlinkapi.profile.dto.ProfileDTO;
import com.ss.heartlinkapi.profile.dto.UpdatePasswordDTO;
import com.ss.heartlinkapi.profile.service.ProfileService;
import com.ss.heartlinkapi.user.entity.ProfileEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;

@RestController
@RequestMapping("/user/profile")
public class ProfileController {

	private final ProfileService profileService;
	private final CoupleService coupleService;
	private final LoginService loginService;

	public ProfileController(ProfileService profileService, CoupleService coupleService, LoginService loginService) {
		this.profileService = profileService;
		this.coupleService = coupleService;
		this.loginService = loginService;
	}
	
	/***************** 로그인한 유저 아이디 반환 ******************/
	@GetMapping("")
	public ResponseEntity<?> selectProfile(@AuthenticationPrincipal CustomUserDetails loginUser) {
		
		if (loginUser == null) {
		    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유저 정보 없음");
		}
		
		try {
		    Long loginUserId = loginUser.getUserId();
		    return ResponseEntity.ok(loginUserId);
		} catch (Exception e) {
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버오류 : "+e);
		}
	}
	
	/***************** 프로필 조회 ******************/
	@GetMapping("/{userId}")
	public ResponseEntity<?> selectProfile(@PathVariable Long userId) {

		UserEntity userEntity = profileService.findByUserId(userId);
		if (userEntity == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다.");
		}

		ProfileEntity userProfile = profileService.selectProfile(userEntity);
		String userimg = userProfile.getProfile_img();
		String bio = userProfile.getBio();
		String loginId = userEntity.getLoginId();
		String nickname = userProfile.getNickname();

		int followingCount = profileService.selectFollowingCount(userId);
		int followersCount = profileService.selectFollowersCount(userId);
		UserEntity coupleUserEntity = coupleService.getCouplePartner(userId);
		Long coupleUserId = coupleUserEntity.getUserId();
		ProfileEntity coupleProfile = profileService.selectProfile(coupleUserEntity);
		String pairimg = coupleProfile.getProfile_img();

		ProfileDTO profileDTO = new ProfileDTO(userimg, pairimg, bio, loginId, nickname, followersCount, followingCount,
				coupleUserId);
		return ResponseEntity.ok(profileDTO);
	}
	

	/***************** 내 프로필에서 비밀번호 변경 ******************/

	@PostMapping("/{userId}/update/password")
	public ResponseEntity<?> updatePassword(@PathVariable Long userId, @RequestBody UpdatePasswordDTO updatePasswordDTO,
			@AuthenticationPrincipal CustomUserDetails loginUser) {

		String beforePassword = updatePasswordDTO.getBeforePassword();
		String afterPassword = updatePasswordDTO.getAfterPassword();

		UserEntity userEntity = profileService.findByUserId(userId);
		if (userEntity == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다.");
		}

		if (!userId.equals(loginUser.getUserId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
		}

		boolean ismatch = loginService.checkPassword(userEntity, beforePassword);
		if (!ismatch) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이전 비밀번호가 일치하지 않습니다.");
		}

		boolean isUpdated = loginService.updatePassword(userEntity, afterPassword);
		if (isUpdated) {
			return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 실패");
		}

	}

	/***************** 애칭 수정 ******************/
	@PostMapping("/{userId}/update/nickname")
	public ResponseEntity<?> updateNickname(@PathVariable Long userId, @RequestBody Map<String, String> request,
			@AuthenticationPrincipal CustomUserDetails loginUser) {

		String nickName = request.get("nickName");

		UserEntity userEntity = profileService.findByUserId(userId);
		if (userEntity == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다.");
		}

		if (!userId.equals(loginUser.getUserId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
		}

		ProfileEntity profileEntity = profileService.findByUserEntity(userEntity);
		if (profileEntity == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("프로필이 존재하지 않습니다.");
		}

		profileEntity.setNickname(nickName);
		profileService.save(profileEntity);

		return ResponseEntity.ok("애칭이 성공적으로 변경되었습니다.");

	}

	/***************** 상태메시지 수정 ******************/
	@PostMapping("/{userId}/update/bio")
	public ResponseEntity<?> updateProfileBio(@PathVariable Long userId, @RequestBody Map<String, String> request,
			@AuthenticationPrincipal CustomUserDetails loginUser) {

		String bio = request.get("bio");

		UserEntity userEntity = profileService.findByUserId(userId);
		if (userEntity == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다.");
		}

		if (!userId.equals(loginUser.getUserId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
		}

		ProfileEntity profileEntity = profileService.findByUserEntity(userEntity);
		if (profileEntity == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("프로필이 존재하지 않습니다.");
		}
		profileEntity.setBio(bio);
		profileService.save(profileEntity);

		return ResponseEntity.ok("상태메시지가 수정되었습니다.");
	}

	/***************** 프로필 이미지 수정 ******************/
	@PostMapping("/{userId}/update/img")
	public ResponseEntity<?> updateProfileImage(@PathVariable Long userId, @RequestParam("img") MultipartFile img,
			@AuthenticationPrincipal CustomUserDetails loginUser) {

		UserEntity userEntity = profileService.findByUserId(userId);
		if (userEntity == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다.");
		}

		if (!userId.equals(loginUser.getUserId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
		}

		if (img.isEmpty()) {
			return ResponseEntity.badRequest().body("이미지가 존재하지 않습니다.");
		}

		try {
			String currentPath = Paths.get("").toAbsolutePath().toString();
			String originalFilename = img.getOriginalFilename();
			// 확장자 확인
			String fileExtension = originalFilename != null
					? originalFilename.substring(originalFilename.lastIndexOf("."))
					: "";
			if (!fileExtension.matches("(?i)\\.(jpg|jpeg|png)$")) {
				return ResponseEntity.badRequest().body("지원하지 않는 파일 형식입니다.");
			}

			// UUID로 파일 이름 변경
			String newFileName = UUID.randomUUID().toString() + fileExtension;
			String filePath = currentPath + "/src/main/resources/static/img/" + newFileName;
			// 파일 생성
			img.transferTo(new File(filePath));
			String imageUrl = "http://localhost:9090/img/" + newFileName;

			ProfileEntity profileEntity = profileService.findByUserEntity(userEntity);

			if (profileEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("프로필이 존재하지 않습니다.");
			}

			profileEntity.setProfile_img(imageUrl);
			profileService.save(profileEntity);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 저장 오류 : " + e);
		}

		return ResponseEntity.ok("프로필 이미지가 성공적으로 업데이트되었습니다.");
	}

}
