package com.ss.heartlinkapi.like.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.heartlinkapi.like.dto.LikeDTO;
import com.ss.heartlinkapi.like.service.LikeService;

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

}
