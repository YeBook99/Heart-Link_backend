package com.ss.heartlinkapi.comment.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.ss.heartlinkapi.post.dto.PostDTO;
import com.ss.heartlinkapi.post.dto.PostFileDTO;
import com.ss.heartlinkapi.post.entity.Visibility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
	
	public CommentDTO(Long commentId2, Long postId2, Long long1, String loginId, String content2, Timestamp createdAt2,
			Timestamp updatedAt2) {
		// TODO Auto-generated constructor stub
	}
	private Long commentId;					// 댓글 아이디
	private Long postId;					// 게시글 아이디
	private Long parentId;					// 부모 댓글 아이디
	private String userId;					// 회원 아이디
	private String content;					// 댓글 내용
	private LocalDateTime createdAt;		// 작성 시간
	private LocalDateTime updatedAt;		// 수정 시간

}
