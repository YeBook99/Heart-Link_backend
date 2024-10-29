package com.ss.heartlinkapi.follow.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.heartlinkapi.follow.dto.FollowerDTO;
import com.ss.heartlinkapi.follow.dto.FollowingDTO;
import com.ss.heartlinkapi.follow.service.FollowService;


@RestController
@RequestMapping("/follow")
public class FollowController {

	private final FollowService followService;

	public FollowController(FollowService followService) {
		this.followService = followService;
	}

	/********** 회원이 팔로우한 유저 목록 보기 **********/
	@GetMapping("/following/{userId}")
	public ResponseEntity<?> getFollowing(@PathVariable Long userId) {
	    try {
	        List<FollowingDTO> followingList = followService.getFollowingByUserId(userId);
	        return ResponseEntity.ok(followingList);
	    } catch (EntityNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저가 존재하지 않습니다.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러 : "+e);
	    }
	}
	
	/********** 회원을 팔로우한 유저 목록 보기 **********/
	@GetMapping("/follower/{userId}")
	public ResponseEntity<?> getFollower(@PathVariable Long userId) {
	    try {
	        List<FollowerDTO> followerList = followService.getFollowersByUserId(userId);
	        return ResponseEntity.ok(followerList);
	    } catch (EntityNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저가 존재하지 않습니다.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러 : " + e);
	    }
	}
	
	
}
