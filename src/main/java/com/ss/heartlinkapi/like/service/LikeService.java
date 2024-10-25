package com.ss.heartlinkapi.like.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.like.dto.LikeDTO;
import com.ss.heartlinkapi.like.entity.LikeEntity;
import com.ss.heartlinkapi.like.repository.LikeRepository;
import com.ss.heartlinkapi.post.dto.PostFileDTO;
import com.ss.heartlinkapi.post.entity.PostFileEntity;
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
                    
                    List<ProfileEntity> profiles = profileRepository.findAllByUserEntity(like.getUserId());

                    return new LikeDTO(
                        like.getLikeId(),
                        like.getUserId().getUserId(),
                        like.getUserId().getLoginId(),
                        (profiles != null) ? profiles.get(0).getProfile_img() : null, // 프로필 이미지 추가
                        postId,
                        null,
                        like.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());
    }

	// 댓글 좋아요 목록 조회
	public List<LikeDTO> getLikesByCommentId(Long commentId) {

		List<LikeEntity> likes = likeRepository.findByPostId_PostId(commentId);
			
			
		return likes.stream()
		            .map(like -> {
		                
		                List<ProfileEntity> profiles = profileRepository.findAllByUserEntity(like.getUserId());
		
		                return new LikeDTO(
		                    like.getLikeId(),
		                    like.getUserId().getUserId(),
		                    like.getUserId().getLoginId(),
		                    (profiles != null) ? profiles.get(0).getProfile_img() : null, // 프로필 이미지 추가
		                    null,
		                    commentId,
		                    like.getCreatedAt()
		                );
		            })
		            .collect(Collectors.toList());
	}
	
	// 내가 누른 좋아요 목록 조회
	public List<PostFileDTO> getPostFilesByUserId(Long userId){
		List<PostFileEntity> postFiles = likeRepository.findPostFilesByUserId(userId);
		
		return postFiles.stream()
				.map(file -> new PostFileDTO(
								file.getPostId().getPostId(),
								file.getFileUrl(),
								file.getFileType(),
								file.getSortOrder()
								))
				.collect(Collectors.toList());

	}

}
