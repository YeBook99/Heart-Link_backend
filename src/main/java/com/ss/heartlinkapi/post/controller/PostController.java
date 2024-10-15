package com.ss.heartlinkapi.post.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.heartlinkapi.post.dto.PostDTO;
import com.ss.heartlinkapi.post.service.PostService;
import com.ss.heartlinkapi.user.entity.UserEntity;

@RestController
@RequestMapping("/feed")
public class PostController {
	
	private final PostService postService;
	
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	// 게시글 작성
	@PostMapping("/write")
	public ResponseEntity<?> writePost(@RequestBody PostDTO postDTO, @AuthenticationPrincipal UserEntity user){
		
		try {
			postService.savePost(postDTO, user);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}

}
