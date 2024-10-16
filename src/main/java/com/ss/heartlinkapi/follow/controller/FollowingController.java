package com.ss.heartlinkapi.follow.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.heartlinkapi.follow.service.FollowService;
import com.ss.heartlinkapi.user.entity.UserEntity;

@RestController
@RequestMapping("/following")
public class FollowingController {

	private final FollowService followService;

	public FollowingController(FollowService followService) {
		this.followService = followService;
	}

	// 로그인한 회원의 팔로잉 찾기
	@GetMapping("/{userId}")
	public List<UserEntity> getFollowing(@PathVariable String userId) {
		return followService.getFollowingIdsByLoginId(userId);
	}

}
