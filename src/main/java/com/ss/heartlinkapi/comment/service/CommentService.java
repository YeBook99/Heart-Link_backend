package com.ss.heartlinkapi.comment.service;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.comment.dto.CommentDTO;
import com.ss.heartlinkapi.comment.entity.CommentEntity;
import com.ss.heartlinkapi.comment.repository.CommentRepository;
import com.ss.heartlinkapi.post.entity.PostEntity;
import com.ss.heartlinkapi.post.repository.PostRepository;
import com.ss.heartlinkapi.user.entity.UserEntity;

@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	
	public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
	}
	
	// 댓글 작성
	@Transactional
	public void writeComment(CommentDTO commentDTO, UserEntity user) {
		
		// 댓글 내용 유효성 검사
        if (commentDTO.getContent() == null || commentDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        }
        
        // PostEntity 가져오기
        PostEntity post = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
		
		CommentEntity comment = new CommentEntity();
		comment.setPostId(post);
		comment.setUserId(user);
		comment.setContent(commentDTO.getContent());
		comment.setUpdatedAt(null);
		
		// 댓글이 대댓글일 경우
		if(commentDTO.getParentId() != null) {
			CommentEntity parentComment = commentRepository.findById(commentDTO.getParentId())
					.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부모 댓글입니다."));
			
			comment.setParentId(parentComment);
		} else {
			comment.setParentId(null);
		}
		
		commentRepository.save(comment);
		
		// 댓글 수 증가
		post.setCommentCount(post.getCommentCount() + 1);
		postRepository.save(post);
				
		
	}
	
}
