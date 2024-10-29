package com.ss.heartlinkapi.follow.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.follow.dto.FollowerDTO;
import com.ss.heartlinkapi.follow.dto.FollowingDTO;
import com.ss.heartlinkapi.follow.entity.FollowEntity;
import com.ss.heartlinkapi.follow.repository.FollowRepository;
import com.ss.heartlinkapi.user.entity.ProfileEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;
import com.ss.heartlinkapi.user.repository.ProfileRepository;

@Service
public class FollowService {

	private final FollowRepository followRepository;
	private final ProfileRepository profileRepository;

	public FollowService(FollowRepository followRepository, ProfileRepository profileRepository) {
		this.followRepository = followRepository;
		this.profileRepository = profileRepository;
	}

	/********** 유저가 팔로우하고 있는 타 유저 목록 **********/
	public List<FollowingDTO> getFollowingByUserId(Long userId) {
		List<FollowEntity> followings = followRepository.findByFollowerUserId(userId);
		
        return followings.stream().map(follow -> {       	
            UserEntity followingUser = follow.getFollowing();
            ProfileEntity followingprofile = profileRepository.findByUserEntity(followingUser);
            
            FollowingDTO following  = new FollowingDTO();
            following.setFollowingUserId(followingUser.getUserId());
            following.setFollowingLoginId(followingUser.getLoginId());
            following.setFollowingImg(followingprofile.getProfile_img());
            
            return following;
            
        }).collect(Collectors.toList());
	
	}
	/********** 유저를 팔로우하고 있는 타 유저 목록 **********/
	public List<FollowerDTO> getFollowersByUserId(Long userId) {
	    List<FollowEntity> followers = followRepository.findByFollowingUserId(userId);

	    return followers.stream().map(follow -> {
	        UserEntity followerUser = follow.getFollower();
	        ProfileEntity followerProfile = profileRepository.findByUserEntity(followerUser);

	        FollowerDTO follower = new FollowerDTO();
	        follower.setFollowerUserId(followerUser.getUserId());
	        follower.setFollowerLoginId(followerUser.getLoginId());
	        follower.setFollowerImg(followerProfile.getProfile_img());

	        return follower;
	    }).collect(Collectors.toList());
	}
	

}
