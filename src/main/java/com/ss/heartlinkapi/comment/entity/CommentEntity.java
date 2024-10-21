package com.ss.heartlinkapi.comment.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ss.heartlinkapi.post.entity.PostEntity;
import com.ss.heartlinkapi.post.entity.Visibility;
import com.ss.heartlinkapi.user.entity.UserEntity;

import lombok.Data;

@Entity
@Data
@Table(name = "comment")
@EntityListeners(AuditingEntityListener.class)
public class CommentEntity {
	@Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId;					// 댓글 아이디
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private PostEntity postId;				// 게시글 아이디
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private CommentEntity parentId;			// 부모 댓글 아이디
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity userId;				// 회원 아이디
	
	@Column(name = "content", nullable= false)
	private String content;					// 댓글 내용
	
	@CreatedDate
	@Column(name = "created_at", nullable= false, updatable = false)
	private Timestamp createdAt;			// 작성 시간
	
	@LastModifiedDate
	@Column(name = "update_at")
	private Timestamp updatedAt;			// 수정 시간
	
	

}
