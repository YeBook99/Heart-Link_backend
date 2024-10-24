package com.ss.heartlinkapi.comment.service;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.comment.dto.CommentDTO;
import com.ss.heartlinkapi.comment.entity.CommentEntity;
import com.ss.heartlinkapi.comment.repository.CommentRepository;

@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	
	public CommentService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	// 게시글 댓글 가져오기
//	@Transactional
//	public List<CommentDTO> getComment(Long postId) {
//		List<CommentEntity> comments = commentRepository.findByPostId(postId);
//		
//		return comments.stream()
//			    .map(comment -> new CommentDTO(
//			        comment.getCommentId(),
//			        comment.getPostId().getPostId(), // PostEntity에서 Long으로
//			        comment.getParentId() != null ? comment.getParentId().getCommentId() : null, // 부모 댓글 ID
//			        comment.getUserId().getUserId(), // UserEntity에서 Long으로
//			        comment.getContent(),
//			        comment.getCreatedAt(),
//			        comment.getUpdatedAt()
//			    ))
//			    .collect(Collectors.toList());
//
//	}
}
