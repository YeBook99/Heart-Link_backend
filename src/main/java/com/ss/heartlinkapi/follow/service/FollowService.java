package com.ss.heartlinkapi.follow.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.couple.repository.CoupleRepository;
import com.ss.heartlinkapi.follow.dto.FollowerDTO;
import com.ss.heartlinkapi.follow.dto.FollowingDTO;
import com.ss.heartlinkapi.follow.entity.FollowEntity;
import com.ss.heartlinkapi.follow.repository.FollowRepository;
import com.ss.heartlinkapi.user.entity.ProfileEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;
import com.ss.heartlinkapi.user.repository.ProfileRepository;
import com.ss.heartlinkapi.user.repository.UserRepository;

@Service
public class FollowService {

	private final FollowRepository followRepository;
	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;
	private final CoupleRepository coupleRepository;

	public FollowService(FollowRepository followRepository, ProfileRepository profileRepository,
			UserRepository userRepository, CoupleRepository coupleRepository) {
		this.followRepository = followRepository;
		this.profileRepository = profileRepository;
		this.userRepository = userRepository;
		this.coupleRepository = coupleRepository;
	}

	/********** 유저가 팔로우하고 있는 타 유저 목록 **********/
	@Transactional(readOnly = true)
	public List<FollowingDTO> getFollowingByUserId(Long userId, Long loginUserId) {
		List<FollowEntity> followings = followRepository.findByFollowerUserId(userId);

		return followings.stream().map(follow -> {

			UserEntity followingUser = follow.getFollowing();
			ProfileEntity followingprofile = profileRepository.findByUserEntity(followingUser);

			FollowingDTO following = new FollowingDTO();

			following.setUserId(loginUserId);
			following.setFollowingUserId(followingUser.getUserId());
			following.setFollowingLoginId(followingUser.getLoginId());
			following.setFollowingImg(followingprofile.getProfile_img());
			following.setStatus(follow.isStatus());

			return following;

		}).collect(Collectors.toList());

	}

	/********** 유저를 팔로우하고 있는 타 유저 목록 **********/
	@Transactional(readOnly = true)
	public List<FollowerDTO> getFollowersByUserId(Long userId, Long loginUserId) {
		List<FollowEntity> followers = followRepository.findByFollowingUserId(userId);

		return followers.stream().map(follow -> {
			UserEntity followerUser = follow.getFollower();
			ProfileEntity followerProfile = profileRepository.findByUserEntity(followerUser);

			FollowerDTO follower = new FollowerDTO();
			follower.setUserId(loginUserId);
			follower.setFollowerUserId(followerUser.getUserId());
			follower.setFollowerLoginId(followerUser.getLoginId());
			follower.setFollowerImg(followerProfile.getProfile_img());
			follower.setStatus(follow.isStatus());

			return follower;
		}).collect(Collectors.toList());
	}
	
	/********** 언팔로우 **********/
	@Transactional
	public void unfollow(UserEntity follower, UserEntity following) {

		FollowEntity followEntity = followRepository.findByFollowerAndFollowing(follower, following);
		if (followEntity != null) {
			followRepository.delete(followEntity);
		} else {
			throw new EntityNotFoundException("팔로우 관계가 존재하지 않습니다.");
		}

	}

	/********** 비공계 계정이라면 팔로우 요청, 아니라면 팔로우 하는 메서드 **********/
	@Transactional
	public void follow(UserEntity follower, UserEntity following) {
		
	    if (followRepository.existsByFollowerAndFollowing(follower, following)) {
	        throw new IllegalArgumentException("이미 팔로우 중입니다.");
	    }
		
		CoupleEntity couple = coupleRepository.findCoupleByUserId(following.getUserId());
		if (couple == null) {
			throw new EntityNotFoundException();
		}

		FollowEntity followEntity = new FollowEntity();

		followEntity.setFollower(follower);
		followEntity.setFollowing(following);
		if (couple.getIsPrivate()) {
			followEntity.setStatus(false);
		}
		followRepository.save(followEntity);
	}

	public UserEntity findByUserId(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}

	/********** 팔로우 요청을 수락하는 메서드 **********/
	@Transactional
	public void acceptFollow(UserEntity follower, UserEntity following) {

		FollowEntity followEntity = followRepository.findByFollowerAndFollowing(follower, following);

		if (followEntity == null) {
			throw new EntityNotFoundException("팔로우 요청이 존재하지 않습니다.");
		}

		if (followEntity.isStatus()) {
			throw new IllegalArgumentException("이미 수락된 요청입니다.");
		}

		followEntity.setStatus(true);
		followRepository.save(followEntity);
	}
	
	/********** 팔로우 요청을 거절하는 메서드 **********/
	public void rejectFollow(UserEntity follower, UserEntity following) {
		FollowEntity followEntity = followRepository.findByFollowerAndFollowing(follower, following);

		if (followEntity == null) {
			throw new EntityNotFoundException("팔로우 요청이 존재하지 않습니다.");
		}
		
		if (followEntity.isStatus()) {
			throw new IllegalArgumentException("이미 수락된 요청입니다.");
		}
		
		followRepository.delete(followEntity);
		
	}

}
