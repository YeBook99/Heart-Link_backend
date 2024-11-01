package com.ss.heartlinkapi.contentLinktag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.comment.entity.CommentEntity;
import com.ss.heartlinkapi.contentLinktag.entity.ContentLinktagEntity;

public interface ContentLinktagRepository extends JpaRepository<ContentLinktagEntity, Long>{

	// 댓글 수정 시 링크태그 데이터 삭제
	void deleteByCommentId(CommentEntity comment);

}
