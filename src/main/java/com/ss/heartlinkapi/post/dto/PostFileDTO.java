package com.ss.heartlinkapi.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostFileDTO {
	
	private Long postId;		// 게시글 id
	private String fileUrl; 	// 파일 경로
	private String file_type; 	// 파일 타입
	private int sortOrder;		// 정렬 순서
	

}
