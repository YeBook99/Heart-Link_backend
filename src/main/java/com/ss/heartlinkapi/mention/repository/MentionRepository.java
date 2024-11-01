package com.ss.heartlinkapi.mention.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.comment.entity.CommentEntity;
import com.ss.heartlinkapi.mention.entity.MentionEntity;

public interface MentionRepository extends JpaRepository<MentionEntity, Long>{

	// 댓글 수정 시 멘션 데이터 삭제
	void deleteByCommentId(CommentEntity comment);

}
