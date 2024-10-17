package com.ss.heartlinkapi.follow.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.follow.dto.FollowDTO;
import com.ss.heartlinkapi.follow.entity.FollowEntity;
import com.ss.heartlinkapi.follow.repository.FollowRepository;
import com.ss.heartlinkapi.user.entity.UserEntity;

@Service
public class FollowService {

	private final FollowRepository followRepository;

	public FollowService(FollowRepository followRepository) {
		this.followRepository = followRepository;
	}

	// 로그인한 회원의 팔로잉 회원 정보
	public List<FollowDTO> getFollowingIdsByUserId(Long userId) {
		List<FollowEntity> follows = followRepository.findFollowingIdsByFollowerId(userId);

		return follows.stream().map(follow -> new FollowDTO(follow.getFollowing().getLoginId()))
				.collect(Collectors.toList());
	}

}
