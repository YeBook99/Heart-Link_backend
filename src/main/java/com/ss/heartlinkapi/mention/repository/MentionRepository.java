package com.ss.heartlinkapi.mention.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ss.heartlinkapi.comment.entity.CommentEntity;
import com.ss.heartlinkapi.mention.entity.MentionEntity;
import com.ss.heartlinkapi.post.entity.PostEntity;

import io.lettuce.core.dynamic.annotation.Param;

public interface MentionRepository extends JpaRepository<MentionEntity, Long>{

	// 댓글 수정 시 멘션 데이터 삭제
	void deleteByCommentId(CommentEntity comment);

	// 게시글 수정 시 멘션 데이터 삭제
	void deleteByPostId(PostEntity post);
	
	// 게시글에 태그된 사용자의 userId만 조회
	@Query("SELECT m FROM MentionEntity m WHERE m.postId = :postId")
	List<MentionEntity> findMentionsByPostId(@Param("postId") Long postId);

}
