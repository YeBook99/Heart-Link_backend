package com.ss.heartlinkapi.post.dto;

import com.ss.heartlinkapi.login.dto.JoinDTO;
import com.ss.heartlinkapi.post.entity.Visibility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
	private String content;	// 게시글 내용
	private Visibility visibility; // 게시글 공개 타입(PUBLIC or PRIVATE)
	

}
