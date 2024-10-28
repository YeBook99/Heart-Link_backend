package com.ss.heartlinkapi.post.service;

import java.lang.System.Logger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.comment.dto.CommentDTO;
import com.ss.heartlinkapi.comment.entity.CommentEntity;
import com.ss.heartlinkapi.comment.repository.CommentRepository;
import com.ss.heartlinkapi.comment.service.CommentService;
import com.ss.heartlinkapi.couple.service.CoupleService;
import com.ss.heartlinkapi.post.dto.PostDTO;
import com.ss.heartlinkapi.post.dto.PostFileDTO;
import com.ss.heartlinkapi.post.entity.FileType;
import com.ss.heartlinkapi.post.entity.PostEntity;
import com.ss.heartlinkapi.post.entity.PostFileEntity;
import com.ss.heartlinkapi.post.entity.Visibility;
import com.ss.heartlinkapi.post.repository.PostFileRepository;
import com.ss.heartlinkapi.post.repository.PostRepository;
import com.ss.heartlinkapi.user.entity.ProfileEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;
import com.ss.heartlinkapi.user.repository.ProfileRepository;
import com.ss.heartlinkapi.user.repository.UserRepository;


@Service
public class PostService {

	private final PostRepository postRepository;
	private final PostFileRepository postFileRepository;
	private final CoupleService coupleService;
	private final CommentRepository commentRepository;
	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;
	private final PostFileService postFileService;

	public PostService(PostRepository postRepository, PostFileRepository postFileRepository, CoupleService coupleService, CommentRepository commentRepository, ProfileRepository profileRepository, UserRepository userRepository, PostFileService postFileService) {
		this.postRepository = postRepository;
		this.postFileRepository = postFileRepository;
		this.coupleService = coupleService;
		this.commentRepository = commentRepository;
		this.profileRepository = profileRepository;
		this.userRepository = userRepository;
		this.postFileService = postFileService;
	}

	// 게시글 작성
	@Transactional
	public void savePost(PostDTO postDTO, UserEntity user) {
		
		List<PostFileDTO> fileList = postDTO.getFiles();
		
		
		PostEntity post = new PostEntity();

		post.setUserId(user);
		post.setContent(postDTO.getContent());
		post.setVisibility(postDTO.getVisibility());
		post.setCreatedAt(LocalDateTime.now());
		post.setLikeCount(0);
		post.setCommentCount(0);
		

		postRepository.save(post);

		
			int sortOrder = 1;
			for (PostFileDTO postFileDTO : fileList) {
				PostFileEntity postFile = new PostFileEntity();
				postFile.setPostId(post);
				postFile.setFileUrl(postFileDTO.getFileUrl());
				postFile.setFileType(postFileDTO.getFileType());
				postFile.setSortOrder(sortOrder);

				postFileRepository.save(postFile);
				sortOrder++;
			}

	}
	
	// 내 팔로잉 게시물 조회
	public List<PostDTO> getPublicPostByFollowerId(Long followerId) {
	    List<PostEntity> posts = postRepository.findPublicPostsByFollowerId(followerId);
	    return posts.stream()
	                .map(post -> {
	                    List<PostFileEntity> postFiles = postFileRepository.findByPostId(post.getPostId());
	                    List<ProfileEntity> profiles = profileRepository.findAllByUserEntity(post.getUserId());
	                    UserEntity partner = coupleService.getCouplePartner(post.getUserId().getUserId());
	                    return new PostDTO(
	                            post.getPostId(),
	                            post.getUserId().getLoginId(),
	                            post.getContent(),
	                            post.getCreatedAt(),
	                            post.getUpdatedAt(),
	                            post.getLikeCount(),
	                            post.getCommentCount(),
	                            post.getVisibility(),
	                            (profiles != null) ? profiles.get(0).getProfile_img() : null, // 프로필 이미지 추가
	                            postFiles.stream()
	                                .map(file -> new PostFileDTO(
	                                    post.getPostId(),
	                                    file.getFileUrl(),
	                                    file.getFileType(),
	                                    file.getSortOrder()))
	                                .collect(Collectors.toList()),
	                                null,
		                    partner != null ? partner.getLoginId() : "No Partner"
		                );
	                })
	                .collect(Collectors.toList());
	}

	
	// 팔로우하지 않은 사용자 게시글 조회
	public List<PostDTO> getNonFollowedAndNonReportedPosts(Long userId) {
	    List<PostEntity> posts = postRepository.findNonFollowedAndNonReportedPosts(userId);
	 
	    return posts.stream()
	    		.map(post -> {
	    			List<PostFileEntity> postFiles = postFileRepository.findByPostId(post.getPostId());
	    			List<ProfileEntity> profiles = profileRepository.findAllByUserEntity(post.getUserId());
	    			UserEntity partner = coupleService.getCouplePartner(post.getUserId().getUserId());
	    			return new PostDTO(
	    					post.getPostId(),
                            post.getUserId().getLoginId(),
                            post.getContent(),
                            post.getCreatedAt(),
                            post.getUpdatedAt(),
                            post.getLikeCount(),
                            post.getCommentCount(),
                            post.getVisibility(),
                            (profiles != null) ? profiles.get(0).getProfile_img() : null, // 프로필 이미지 추가
                            postFiles.stream()
                                .map(file -> new PostFileDTO(
                                    post.getPostId(),
                                    file.getFileUrl(),
                                    file.getFileType(),
                                    file.getSortOrder()))
                                .collect(Collectors.toList()),
                                null,
	                    partner != null ? partner.getLoginId() : "No Partner"
	    					);
	    		})
	    		.collect(Collectors.toList());
	}
	
	// 게시글 상세보기
	public PostDTO getPostById(Long postId, Long userId) {
	    Optional<PostEntity> optionalPost = postRepository.findById(postId);
	    
	    // 값이 존재하는 경우
	    if (optionalPost.isPresent()) {
	        PostEntity post = optionalPost.get();
	        List<PostFileEntity> postFiles = postFileRepository.findByPostId(post.getPostId());
	        List<ProfileEntity> profiles = profileRepository.findAllByUserEntity(post.getUserId());
	        UserEntity partner = coupleService.getCouplePartner(post.getUserId().getUserId());
	        
	        // 댓글 목록 가져오기
	        List<CommentEntity> comments = commentRepository.findByPostIdAndNotReported(post, userId);
	        List<CommentDTO> commentDTO = comments.stream()
	            .map(comment -> {
	            	List<ProfileEntity> commentProfiles = profileRepository.findAllByUserEntity(comment.getUserId());
	                String profileImage = (commentProfiles != null && !commentProfiles.isEmpty()) ? commentProfiles.get(0).getProfile_img() : null;
	            	
	            return new CommentDTO(
	                comment.getCommentId(),
	                comment.getPostId().getPostId(),
	                comment.getParentId() != null ? comment.getParentId().getCommentId() : null,
	                comment.getUserId().getUserId(),
	                comment.getContent(),
	                comment.getCreatedAt(),
	                comment.getUpdatedAt(),
	                comment.getUserId().getLoginId(),
	                profileImage
	            );
	        })
	        .collect(Collectors.toList());
	        
	        return new PostDTO(
	            post.getPostId(),
	            post.getUserId().getLoginId(),
	            post.getContent(),
	            post.getCreatedAt(),
	            post.getUpdatedAt(),
	            post.getLikeCount(),
	            post.getCommentCount(),
	            post.getVisibility(),
	            (profiles != null) ? profiles.get(0).getProfile_img() : null, // 프로필 이미지 추가
	            postFiles.stream()
	                .map(file -> new PostFileDTO(
	                    post.getPostId(),
	                    file.getFileUrl(),
	                    file.getFileType(),
	                    file.getSortOrder()))
	                .collect(Collectors.toList()),
	            commentDTO.isEmpty() ? Collections.emptyList() : commentDTO, // 댓글이 없으면 빈 리스트
	            partner != null ? partner.getLoginId() : "No Partner"
	        );
	    } else {
	        throw new NoSuchElementException("해당 게시글을 찾을 수 없습니다.");
	    }
	}
	
	// 사용자와 사용자의 커플 게시글 목록 가져오기
	public List<PostFileDTO> getPostFilesByUserId(Long userId){
		UserEntity partner = coupleService.getCouplePartner(userId);
		List<PostFileEntity> myPostFiles = postFileRepository.findPostFilesByUserId(userId);
		
		List<PostFileEntity> partnerPostFiles = new ArrayList<>();
	    if (partner != null) {
	        partnerPostFiles = postFileRepository.findPostFilesByUserId(partner.getUserId());
	    }
		
		List<PostFileEntity> allPostFiles = new ArrayList<>();
		allPostFiles.addAll(myPostFiles);
		allPostFiles.addAll(partnerPostFiles);
		
		
		return allPostFiles.stream()
				.map(file -> new PostFileDTO(
								file.getPostId().getPostId(),
								file.getFileUrl(),
								file.getFileType(),
								file.getSortOrder()
								))
				.collect(Collectors.toList());
		
	}
	
	// 게시글 삭제
	public void deleteMyPost(Long postId, Long userId) {
		PostEntity post = postRepository.findByPostIdAndUserId_UserId(postId, userId);
		
		if (post != null) {
			postRepository.delete(post);
		} else {
			throw new RuntimeException("게시글이 존재하지 않거나 접근 권한이 없습니다.");
		}

	}
	
	// 모든 게시글 삭제
	@Transactional
	public void deleteAllPostByUser(Long userId) {
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		
		postRepository.deleteByUserId(user);
		
	}
	
	// 게시글 수정
	@Transactional
	public void updatePost(Long postId, String newContent, Visibility newVisibility, List<String> newFileUrls, List<String> newFileType, int newSortOrder) {
		PostEntity post = postRepository.findById(postId)
				.orElseThrow(() -> new EntityNotFoundException("Post not found"));
		
		// 게시글 내용 수정
		if(newContent != null) {
			post.setContent(newContent);
		}
		// 공개 범위 수정
		if(newFileUrls != null) {
			List<PostFileEntity> postFiles = postFileRepository.findByPostId(postId);
			
			for (int i = 0; i < newFileUrls.size(); i++) {
				PostFileEntity postFile;
				
				if(i < postFiles.size()) {
					// 기존 파일 업데이트
					postFile = postFiles.get(i);
				} else {
					// 새 파일 추가
					postFile = new PostFileEntity();
					postFile.setPostId(post);
					postFile.setSortOrder(i + 1);
				}
				
				String newFileUrl = newFileUrls.get(i);
				postFile.setFileUrl(newFileUrl);
				postFile.setFileType(postFileService.determineFileType(newFileUrl));
				
				postFileRepository.save(postFile);
				
			}
		}
		
		post.setUpdatedAt(LocalDateTime.now());
		postRepository.save(post);
		
	}
	


//	관리자 신고한 게시물 삭제
    public void deletePost(Long postId) {
		postRepository.deleteById(postId);
    }
}
