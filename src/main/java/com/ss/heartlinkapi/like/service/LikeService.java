package com.ss.heartlinkapi.like.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.like.dto.LikeDTO;
import com.ss.heartlinkapi.like.entity.LikeEntity;
import com.ss.heartlinkapi.like.repository.LikeRepository;
import com.ss.heartlinkapi.user.entity.ProfileEntity;
import com.ss.heartlinkapi.user.repository.ProfileRepository;

@Service
public class LikeService {
	
	private final LikeRepository likeRepository;
	private final ProfileRepository profileRepository;
	
	public LikeService(LikeRepository likeRepository, ProfileRepository profileRepository) {
		this.likeRepository = likeRepository;
		this.profileRepository = profileRepository;
	}
	
	// 게시글 좋아요 목록 조회
	@Transactional
	public List<LikeDTO> getLikesByPostId(Long postId) {
		List<LikeEntity> likes = likeRepository.findByPostId_PostId(postId);
		
		
		return likes.stream()
                .map(like -> {
                    Long userId = like.getUserId().getUserId();
                    String loginId = like.getUserId().getLoginId();
                    
                    List<ProfileEntity> profiles = profileRepository.findAllByUserEntity(like.getUserId());
                    String profileImg = (profiles != null) ? profiles.get(0).getProfile_img() : null;

                    return new LikeDTO(
                        like.getLikeId(),
                        userId,
                        loginId,
                        profileImg, // 프로필 이미지 추가
                        postId,
                        null,
                        like.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());
    }
	

}
