package com.ss.heartlinkapi.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.comment.entity.CommentEntity;
import com.ss.heartlinkapi.post.entity.PostEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long>{

	// 게시글 댓글 보기
	List<CommentEntity> findByPostId(PostEntity postId);
}
