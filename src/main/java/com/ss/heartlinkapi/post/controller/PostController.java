package com.ss.heartlinkapi.post.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.heartlinkapi.bookmark.service.BookmarkService;
import com.ss.heartlinkapi.like.service.LikeService;
import com.ss.heartlinkapi.login.dto.CustomUserDetails;
import com.ss.heartlinkapi.post.dto.PostDTO;
import com.ss.heartlinkapi.post.dto.PostFileDTO;
import com.ss.heartlinkapi.post.dto.PostUpdateDTO;
import com.ss.heartlinkapi.post.entity.PostEntity;
import com.ss.heartlinkapi.post.entity.PostFileEntity;
import com.ss.heartlinkapi.post.service.PostService;
import com.ss.heartlinkapi.user.entity.UserEntity;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/feed")
public class PostController {
	
	private final PostService postService;
	private final LikeService likeService;
	private final BookmarkService bookmarkService;
	
	public PostController(PostService postService, LikeService likeService, BookmarkService bookmarkService) {
		this.postService = postService;
		this.likeService = likeService;
		this.bookmarkService = bookmarkService;
	}
	
	
	// 게시글 작성
	@PostMapping("/write")
	public ResponseEntity<?> writePost(
	        @RequestParam("post") String postJson, // JSON 문자열로 받음
	        @RequestParam("files") List<MultipartFile> files,
	        @AuthenticationPrincipal CustomUserDetails user) throws JsonMappingException, JsonProcessingException {
	    ObjectMapper objectMapper = new ObjectMapper();
	    PostDTO postDTO = objectMapper.readValue(postJson, PostDTO.class);

	    Long userId = user.getUserId();
	    
	    UserEntity myId = new UserEntity();
	    myId.setUserId(userId);

	    // 첨부파일이 없을 때 예외
	    if (files == null || files.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("첨부파일이 최소 1개 이상 포함되어야 합니다.");
	    }

	    try {
	        postService.savePost(postDTO, files, myId);
	        return ResponseEntity.status(HttpStatus.CREATED).build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 작성 중 오류가 발생하였습니다.");
	    }
	}

	
	// 내 게시물 조회
	@GetMapping("")
	public ResponseEntity<?> getFollowingPublicPosts(@AuthenticationPrincipal CustomUserDetails user){
		Long userId = user.getUserId();
		
		List<PostDTO> followingPosts = postService.getPublicPostByFollowerId(userId);
		List<PostDTO> nonFollowedPosts = postService.getNonFollowedAndNonReportedPosts(userId);
		
		return ResponseEntity.ok().body(new PostsResponse(followingPosts, nonFollowedPosts));
		
	}
	
	private static class PostsResponse {
		private List<PostDTO> followingPosts;
		private List<PostDTO> nonFollowedPosts;
		
		public PostsResponse(List<PostDTO> followingPosts, List<PostDTO> nonFollowedPosts) {
			this.followingPosts = followingPosts;
			this.nonFollowedPosts = nonFollowedPosts;
		}
		
		public List<PostDTO> getFollowingPosts(){
			return followingPosts;
		}
		
		public List<PostDTO> getNonFollowedPosts(){
			return nonFollowedPosts;
		}
		
	}
	
	// 게시글 상세보기
	@GetMapping("/details/{postId}")
	public ResponseEntity<PostDTO> getPostWithComments(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails user){
		
		Long userId = user.getUserId();
		
		PostDTO postDTO = postService.getPostById(postId, userId);
		
		return ResponseEntity.ok(postDTO);
	}
	
	// 내가 누른 좋아요 목록 조회
	@GetMapping("/like")
	public ResponseEntity<List<PostFileDTO>> getLikePostFilesByUserId(@AuthenticationPrincipal CustomUserDetails user) {
		
		Long userId = user.getUserId();
	    List<PostFileDTO> postFiles = likeService.getPostFilesByUserId(userId); 				   
	    
	    return ResponseEntity.ok(postFiles);
	}
	
	// 내가 누른 북마크 목록 조회
	@GetMapping("/bookmark")
	public ResponseEntity<List<PostFileDTO>> getBokkmarkPostFilesByUserId(@AuthenticationPrincipal CustomUserDetails user){
		
		Long userId = user.getUserId();
		
		List<PostFileDTO> postFiles = bookmarkService.getBokkmarkPostFilesByUserId(userId);
		
		return ResponseEntity.ok(postFiles);
		
	}
	
	
	// 사용자와 사용자의 커플 게시글 목록 조회
	@GetMapping("/couple")
	public ResponseEntity<List<PostFileDTO>> getCouplePostFiles(@AuthenticationPrincipal CustomUserDetails user){
		
		Long userId =user.getUserId();
		
		List<PostFileDTO> postFiles = postService.getPostFilesByUserId(userId);
		
		return ResponseEntity.ok(postFiles);
	}
	
	// 내 게시글 삭제
	@DeleteMapping("/{postId}/delete")
	public ResponseEntity<?> deletePost(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails user){
		Long userId = user.getUserId();
		
		postService.deleteMyPost(postId, userId);
		
		return ResponseEntity.ok("게시글 삭제 완료");
		
	}
	
	// 모든 게시글 삭제
	@DeleteMapping("/user")
	public ResponseEntity<?> deleteAllPostsByUser(@AuthenticationPrincipal CustomUserDetails user){
		Long userId = user.getUserId();
		
		postService.deleteAllPostByUser(userId);
		return ResponseEntity.ok("모든 게시글 삭제 완료");
	}
	
	// 게시글 수정
	@PutMapping("/{postId}/update")
	public ResponseEntity<?> updatePost(
			@PathVariable Long postId,
		    @RequestBody PostUpdateDTO postUpdateDTO,
		    @AuthenticationPrincipal CustomUserDetails user){
		
		Long userId = user.getUserId();
		
		try {
			postService.updatePost(postId, userId, postUpdateDTO);
			return ResponseEntity.ok("게시글 수정 완료");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	



}
