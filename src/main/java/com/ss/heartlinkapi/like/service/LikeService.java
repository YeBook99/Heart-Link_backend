package com.ss.heartlinkapi.like.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.like.dto.LikeDTO;
import com.ss.heartlinkapi.like.entity.LikeEntity;
import com.ss.heartlinkapi.like.repository.LikeRepository;

@Service
public class LikeService {
	
	private final LikeRepository likeRepository;
	
	public LikeService(LikeRepository likeRepository) {
		this.likeRepository = likeRepository;
	}
	
	// 게시글 좋아요 목록 조회
	@Transactional
	public List<LikeDTO> getLikesByPostId(Long postId) {
		List<LikeEntity> likes = likeRepository.findByPostId_PostId(postId);
		
		return likes.stream()
					.map(like -> new LikeDTO(
						like.getLikeId(),
						like.getUserId().getUserId(),
						like.getUserId().getLoginId(),
						like.getUserId().getProfileEntity(),
						postId,
						null,
						like.getCreatedAt()
					))
					.collect(Collectors.toList());
	}
	

}
