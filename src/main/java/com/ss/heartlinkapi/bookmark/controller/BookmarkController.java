package com.ss.heartlinkapi.bookmark.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.heartlinkapi.bookmark.service.BookmarkService;

@RestController
@RequestMapping("/bookmark")
public class BookmarkController {
	
	private final BookmarkService bookmarkService;
	
	public BookmarkController(BookmarkService bookmarkService) {
		this.bookmarkService = bookmarkService;
	}
	
	@PostMapping("/{postId}")
	public ResponseEntity<String> toggleBookmark(Long postId, @AuthenticationPrincipal UserDetails user) {
	    Long userId = 1L; // user.getUserId(); // userDetails에서 userId 추출
	    boolean result = bookmarkService.addOrRemoveBookmark(postId, userId);

	    return ResponseEntity.ok(result ? "북마크 추가됨" : "북마크 삭제됨");
	}

	

}
