package com.ss.heartlinkapi.like.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ss.heartlinkapi.like.dto.LikeDTO;
import com.ss.heartlinkapi.like.service.LikeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/like")
public class LikeController {
	
	private LikeService likeService;
	
	public LikeController(LikeService likeService) {
		this.likeService = likeService;
	}
	
	// 게시글의 좋아요 목록 조회
    @GetMapping("/{postId}/users")
    public ResponseEntity<List<LikeDTO>> getLikesByPostId(@PathVariable Long postId) {
        List<LikeDTO> likes = likeService.getLikesByPostId(postId);
        return ResponseEntity.ok(likes);
    }

    // 댓글의 좋아요 목록 조회
    @GetMapping("/comment/{commentId}/users")
    public ResponseEntity<List<LikeDTO>> getLikesByCommentId(@PathVariable Long commentId) {
        List<LikeDTO> likes = likeService.getLikesByCommentId(commentId);
        return ResponseEntity.ok(likes);
    }
    
    @PostMapping("/{postId}/{userId}")
    public ResponseEntity<Void> likePost(@PathVariable Long userId, @PathVariable Long postId) {
        likeService.likePost(userId, postId);
        return ResponseEntity.ok().build();
    }

}
