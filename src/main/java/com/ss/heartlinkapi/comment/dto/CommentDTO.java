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
	
    private Long commentId;     // 댓글 아이디
    private Long postId;        // 게시글 아이디
    private Long parentId;      // 부모 댓글 아이디
    private Long userId;      // 회원 아이디
    private String content;      // 댓글 내용
    private LocalDateTime createdAt; // 작성 시간
    private LocalDateTime updatedAt; // 수정 시간
}
