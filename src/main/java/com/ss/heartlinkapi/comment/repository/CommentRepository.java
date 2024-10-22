package com.ss.heartlinkapi.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.comment.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long>{

	// 특정 게시글 댓글 출력
	List<CommentEntity> findByPostId(Long postId);
}
