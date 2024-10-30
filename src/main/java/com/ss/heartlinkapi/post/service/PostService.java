package com.ss.heartlinkapi.post.service;

import java.io.File;
import java.lang.System.Logger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.apache.kafka.common.Uuid;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ss.heartlinkapi.comment.dto.CommentDTO;
import com.ss.heartlinkapi.comment.entity.CommentEntity;
import com.ss.heartlinkapi.comment.repository.CommentRepository;
import com.ss.heartlinkapi.comment.service.CommentService;
import com.ss.heartlinkapi.couple.service.CoupleService;
import com.ss.heartlinkapi.post.dto.PostDTO;
import com.ss.heartlinkapi.post.dto.PostFileDTO;
import com.ss.heartlinkapi.post.dto.PostUpdateDTO;
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

import io.jsonwebtoken.io.IOException;


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
	public void savePost(PostDTO postDTO, List<MultipartFile> files, UserEntity user) {
		
		List<PostFileDTO> fileList = postDTO.getFiles();
		
		
		PostEntity post = new PostEntity();

		post.setUserId(user);
		post.setContent(postDTO.getContent());
		post.setVisibility(postDTO.getVisibility());
		post.setCreatedAt(LocalDateTime.now());
		post.setLikeCount(0);
		post.setCommentCount(0);
		

		postRepository.save(post);
		
		// 파일 저장 경로 지정
		String uploadDir = "src/main/resources/static/img/";
		
		int sortOrder = 1;
		
		for (MultipartFile file : files) {
			if(!file.isEmpty()) {
				try {
					// 파일 확장자 추출 및 검증
					String originalFileName = file.getOriginalFilename();
					String fileExtension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";
					
					if (!fileExtension.matches("(?!)\\.(jpg|jpeg|png)$")) {
						throw new IllegalArgumentException("지원하지 않는 파일 형식입니다.");
					}
					
					// 파일 이름에 UUID 추가
					String newFileName = UUID.randomUUID().toString() + fileExtension;
					File destinationFile = new File(uploadDir + newFileName);
					file.transferTo(destinationFile);
					
					// 파일 URL 생성
					String fileUrl = "src/main/resources/static/img/" + newFileName;
					
					// PostFileEntity 생성
					PostFileEntity postFile = new PostFileEntity();
					postFile.setPostId(post);
					postFile.setFileUrl(fileUrl);
					postFile.setFileType(postFileService.determineFileType(fileExtension));
					postFile.setSortOrder(sortOrder++);
					
					postFileRepository.save(postFile);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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
	                            post.getUserId().getUserId(),
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
		                    partner != null ? partner.getLoginId() : "No Partner",
		             	    partner != null ? partner.getUserId() : null
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
	    					post.getUserId().getUserId(),
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
	                    partner != null ? partner.getLoginId() : "No Partner",
	             	    partner != null ? partner.getUserId() : null
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
	            post.getUserId().getUserId(),
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
	            partner != null ? partner.getLoginId() : "No Partner",
	           partner != null ? partner.getUserId() : null
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
	public void updatePost(Long postId, Long userId, PostUpdateDTO updateDTO) {
	    // 게시글 조회 및 권한 확인
	    PostEntity post = postRepository.findById(postId)
	            .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));

	    if (!post.getUserId().getUserId().equals(userId)) {
	        throw new IllegalArgumentException("권한이 없습니다. 게시글 작성자와 동일한 사용자가 아닙니다.");
	    }

	    // 게시글 내용 및 가시성 업데이트
	    post.setContent(updateDTO.getContent());
	    post.setVisibility(updateDTO.getVisibility());

	    // 새로운 파일 URL 목록 가져오기
	    List<String> newFileUrls = Optional.ofNullable(updateDTO.getNewFileUrls()).orElse(Collections.emptyList());

	    // 해당 게시글의 모든 파일 삭제
	    postFileRepository.deleteByPostId(postId);

	    // 새로운 파일 추가 및 정렬 순서 할당
	    int sortOrder = 1;
	    for (String newFileUrl : newFileUrls) {
	        PostFileEntity newFile = new PostFileEntity();
	        newFile.setPostId(post);
	        newFile.setFileUrl(newFileUrl);
	        newFile.setFileType(postFileService.determineFileType(newFileUrl));
	        newFile.setSortOrder(sortOrder++);  // sortOrder 증가

	        postFileRepository.save(newFile);
	    }
	}




	


//	관리자 신고한 게시물 삭제
    public void deletePost(Long postId) {
		postRepository.deleteById(postId);
    }
}
