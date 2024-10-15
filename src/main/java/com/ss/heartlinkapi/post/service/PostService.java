package com.ss.heartlinkapi.post.service;

import java.lang.System.Logger;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.post.dto.PostDTO;
import com.ss.heartlinkapi.post.dto.PostFileDTO;
import com.ss.heartlinkapi.post.entity.PostEntity;
import com.ss.heartlinkapi.post.entity.PostFileEntity;
import com.ss.heartlinkapi.post.repository.PostFileRepository;
import com.ss.heartlinkapi.post.repository.PostRepository;
import com.ss.heartlinkapi.user.entity.UserEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostService {

	private final PostRepository postRepository;
	private final PostFileRepository postFileRepository;

	public PostService(PostRepository postRepository, PostFileRepository postFileRepository) {
		this.postRepository = postRepository;
		this.postFileRepository = postFileRepository;
	}

	// 게시글 작성
	@Transactional
	public void savePost(PostDTO postDTO, UserEntity user) {
		
		PostEntity post = new PostEntity();

		post.setUserId(user);
		post.setContent(postDTO.getContent());
		post.setVisibility(postDTO.getVisibility());
		post.setCreatedAt(LocalDateTime.now());
		post.setLikeCount(0);
		post.setCommentCount(0);

		postRepository.save(post);

		List<PostFileDTO> fileList = postDTO.getFiles();
		if (fileList != null) {
			for (PostFileDTO postFileDTO : fileList) {
				PostFileEntity postFile = new PostFileEntity();
				postFile.setPostId(post);
				postFile.setFileUrl(postFileDTO.getFileUrl());
				postFile.setFileType(postFileDTO.getFileType());
				postFile.setSortOrder(postFileDTO.getSortOrder());

				postFileRepository.save(postFile);
			}
		}

	}

}
