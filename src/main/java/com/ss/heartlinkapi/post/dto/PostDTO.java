package com.ss.heartlinkapi.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ss.heartlinkapi.comment.dto.CommentDTO;
import com.ss.heartlinkapi.post.entity.Visibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
	
	private Long postId;                // 게시글 ID
    private String loginId;             // 작성자 ID (또는 이름)
    private String content;             // 게시글 내용
    private LocalDateTime createdAt;    // 작성 시간
    private LocalDateTime updatedAt;	// 수정 시간
    private int likeCount;				// 좋아요 수
    private int commentCount;			// 댓글 수
    private Visibility visibility;      // 게시글 공개 타입
    private List<PostFileDTO> files;    // 게시글에 첨부된 파일 리스트
    private List<CommentDTO> comments;	// 댓글 리스트
    
    private String partnerId; // 내 커플의 아이디
//    private List<CommentDTO> comments;	// 댓글 리스트
    

}


