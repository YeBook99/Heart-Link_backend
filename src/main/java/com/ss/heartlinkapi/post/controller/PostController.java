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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/feed")
public class PostController {
	
	private final PostService postService;
	
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	// 게시글 작성
//	@PostMapping("/write")
//	public ResponseEntity<?> writePost(@RequestBody PostDTO postDTO, @AuthenticationPrincipal UserEntity user){
//		
//		try {
//			postService.savePost(postDTO, user);
//			return ResponseEntity.status(HttpStatus.CREATED).build();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//		
//	}
	
	// 사용자ID 안받고 하는 임시 게시글 작성 코드
	@PostMapping("/write")
	public ResponseEntity<?> writePost(@RequestBody PostDTO postDTO) {
	    // 임시 UserEntity 생성
	    UserEntity testUser = new UserEntity();
	    testUser.setUserId(1L);  // 임의의 사용자 ID
//	    testUser.setUsername("testUser");  // 임의의 사용자 이름
	    
	    // 첨부파일이 없을 때 예외
	    if (postDTO.getFiles() == null || postDTO.getFiles().isEmpty()) {
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("첨부파일이 최소 1개 이상 포함되어야 합니다.");
	    }
	    
	    try {
	        postService.savePost(postDTO, testUser);
	        return ResponseEntity.status(HttpStatus.CREATED).build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}


}
