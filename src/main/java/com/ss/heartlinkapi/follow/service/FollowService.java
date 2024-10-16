package com.ss.heartlinkapi.follow.service;

import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.follow.repository.FollowRepository;

@Service
public class FollowService {
	
	private final FollowRepository followRepository;
	
	public FollowService(FollowRepository followRepository) {
		this.followRepository = followRepository;
	}

}
