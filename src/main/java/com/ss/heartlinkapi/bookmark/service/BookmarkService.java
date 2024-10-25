package com.ss.heartlinkapi.bookmark.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.bookmark.repository.BookmarkRepository;
import com.ss.heartlinkapi.post.dto.PostFileDTO;
import com.ss.heartlinkapi.post.entity.PostFileEntity;

@Service
public class BookmarkService {
	
	private final BookmarkRepository bookmarkRepository;
	
	public BookmarkService(BookmarkRepository bookmarkRepository) {
		this.bookmarkRepository = bookmarkRepository;
	}
	
	// 내가 누른 북마크 목록 조회
	public List<PostFileDTO> getBokkmarkPostFilesByUserId(Long userId){
		List<PostFileEntity> postFiles = bookmarkRepository.findBokkmarkPostFilesByUserId(userId);
		
		return postFiles.stream()
				.map(file -> new PostFileDTO(
								file.getPostId().getPostId(),
								file.getFileUrl(),
								file.getFileType(),
								file.getSortOrder()
								))
				.collect(Collectors.toList());
		
	}

}
