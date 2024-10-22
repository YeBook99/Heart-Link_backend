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
    public PostDTO(Long postId2, String loginId2, String content2, LocalDateTime createdAt2, LocalDateTime updatedAt2,
			int likeCount2, int commentCount2, Visibility visibility2, List<PostFileDTO> collect, Object object) {
		// TODO Auto-generated constructor stub
	}
	private Long postId;                // 게시글 ID
    private String loginId;             // 작성자 ID (또는 이름)
    private String content;             // 게시글 내용
    private LocalDateTime createdAt;    // 작성 시간
    private LocalDateTime updatedAt;	// 수정 시간
    private int likeCount;				// 좋아요 수
    private int commentCount;			// 댓글 수
    private Visibility visibility;      // 게시글 공개 타입
    private List<PostFileDTO> files;    // 게시글에 첨부된 파일 리스트
    
    private String partnerId; // 내 커플의 아이디
    private List<CommentDTO> comments;	// 댓글 리스트
}
