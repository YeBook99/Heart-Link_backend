package com.ss.heartlinkapi.post.service;

import java.io.File;
import java.lang.System.Logger;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.apache.kafka.common.Uuid;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ss.heartlinkapi.comment.dto.CommentDTO;
import com.ss.heartlinkapi.comment.entity.CommentEntity;
import com.ss.heartlinkapi.comment.repository.CommentRepository;
import com.ss.heartlinkapi.comment.service.CommentService;
import com.ss.heartlinkapi.contentLinktag.entity.ContentLinktagEntity;
import com.ss.heartlinkapi.contentLinktag.repository.ContentLinktagRepository;
import com.ss.heartlinkapi.couple.service.CoupleService;
import com.ss.heartlinkapi.linktag.entity.LinkTagEntity;
import com.ss.heartlinkapi.linktag.repository.LinkTagRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
	private final ContentLinktagRepository contentLinktagRepository;
	private final LinkTagRepository linkTagRepository;

	public PostService(PostRepository postRepository, PostFileRepository postFileRepository, CoupleService coupleService, CommentRepository commentRepository, ProfileRepository profileRepository, UserRepository userRepository, PostFileService postFileService, ContentLinktagRepository contentLinktagRepository, LinkTagRepository linkTagRepository) {
		this.postRepository = postRepository;
		this.postFileRepository = postFileRepository;
		this.coupleService = coupleService;
		this.commentRepository = commentRepository;
		this.profileRepository = profileRepository;
		this.userRepository = userRepository;
		this.postFileService = postFileService;
		this.contentLinktagRepository = contentLinktagRepository;
		this.linkTagRepository = linkTagRepository;
	}

	// 게시글 작성
	@Transactional
	public void savePost(PostDTO postDTO, List<MultipartFile> files, UserEntity user) {
		
		// 파일 개수 제한 검사
	    if (files.size() > 10) {
	        throw new IllegalArgumentException("첨부파일은 최대 10개까지만 허용됩니다.");
	    }
	    
		// VIDEO 파일 개수 제한 검사
	    long videoFileCount = files.stream()
		    .filter(file -> {
		        String originalFileName = file.getOriginalFilename();
		        String fileExtension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";
		        return postFileService.determineFileType(fileExtension) == FileType.VIDEO;
		    })
		    .count();
	    
	    if (videoFileCount > 1) {
	        throw new IllegalArgumentException("비디오 파일은 최대 1개까지만 업로드할 수 있습니다.");
	    }
		
		
	    // PostEntity 생성
	    PostEntity post = new PostEntity();
	    post.setUserId(user);
	    post.setContent(postDTO.getContent());
	    post.setVisibility(postDTO.getVisibility());
	    post.setLikeCount(0);
	    post.setCommentCount(0);

	    // PostEntity 저장
	    postRepository.save(post);
	    

	    // 파일 저장 경로 지정
	    String uploadDir = Paths.get("").toAbsolutePath().toString();
	    
	    int sortOrder = 1; // 정렬 순서 초기화

	    
	    for (MultipartFile file : files) {
	        if (!file.isEmpty()) {
	            try {
	                // 파일 확장자 추출 및 검증
	                String originalFileName = file.getOriginalFilename();
	                String fileExtension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";
	                
	                if (!fileExtension.matches("\\.(jpg|jpeg|png|mp4|avi|mov)$")) {
	                    throw new IllegalArgumentException("지원하지 않는 파일 형식입니다.");
	                }
	                
	                // 파일 이름에 UUID 추가
	                String newFileName = UUID.randomUUID().toString() + fileExtension;
	                File destinationFile = new File(uploadDir + "/src/main/resources/static/img/" + newFileName);
	                file.transferTo(destinationFile);
	                
	                // 파일 URL 생성
	                String fileUrl = "src/main/resources/static/img/" + newFileName;
	                
	                // PostFileEntity 생성
	                PostFileEntity postFile = new PostFileEntity();
	                postFile.setPostId(post); // postId 설정
	                postFile.setFileUrl(fileUrl);
	                postFile.setFileType(postFileService.determineFileType(fileExtension));
	                postFile.setSortOrder(sortOrder++);
	                
	                // PostFileEntity 저장
	                postFileRepository.save(postFile);
	                
	                System.out.println("파일 저장 완료: " + fileUrl);

	            } catch (Exception e) {
	                e.printStackTrace();
	                // 파일 저장 실패 로그 출력
	                System.err.println("파일 저장 실패: " + file.getOriginalFilename() + " - " + e.getMessage());
	            }
	        } else {
	            // 비어 있는 파일 경고 로그 출력
	            System.err.println("비어 있는 파일: " + file.getOriginalFilename());
	        }
	    }
	    
	    // 아이디 태그 및 해시태그 처리
	    processTags(postDTO.getContent(), post);
	}
	
	// 게시글 작성 태그처리
	@Transactional
	private void processTags(String content, PostEntity post) {
		
		// 아이디 태그 처리
		Pattern userPattern = Pattern.compile("@(\\w+)");
		Matcher userMatcher = userPattern.matcher(content);
		
		while(userMatcher.find()) {
			String username = userMatcher.group(1); // @ 생략
			UserEntity user = userRepository.findByLoginId(username);
			
			// 해당 유저 있을 경우
	        if(user != null) {
	            // 기타 처리(알림) ?
	            System.out.println("아이디 태그 처리: " + username);
	         
	        } else {
	            System.out.println("아이디 태그 처리 실패: " + username + "는 존재하지 않는 사용자입니다.");
	        }
		}
		
		// 해시태그 처리
		Pattern linktagPattern = Pattern.compile("&([\\w가-힣]+)");
		Matcher linktagMatcher = linktagPattern.matcher(content);
		
		List<ContentLinktagEntity> contentLinktags = new ArrayList<>();
		
		while(linktagMatcher.find()) {
			System.out.println("해시태그 발견: " + linktagMatcher.group(1)); // 추가된 로깅
			
			String keyword = linktagMatcher.group(1); // & 생략
			// linkTag에 내가 작성한 태그가 없으면 데이터 생성
			LinkTagEntity linkTag = linkTagRepository.findByKeyword(keyword)
					.orElseGet(() -> new LinkTagEntity(null, keyword));
			linkTagRepository.save(linkTag);
			System.out.println("저장된 LinkTag: " + linkTag.getKeyword());
			
			
			ContentLinktagEntity contentLinktag = new ContentLinktagEntity();
			contentLinktag.setLinktagId(linkTag);
			contentLinktag.setBoardId(post);
			contentLinktags.add(contentLinktag);
			
			System.out.println("저장될 ContentLinktag: " + contentLinktag);
		}
		System.out.println("해시태그 처리 종료");
		
		contentLinktagRepository.saveAll(contentLinktags);
		System.out.println("모든 ContentLinktag가 저장되었습니다.");
	}
	


	
	
	// 내 팔로잉 게시물 조회
	public Page<PostDTO> getPublicPostByFollowerId(Long followerId, Pageable pageable) {
	    Page<PostEntity> posts = postRepository.findPublicPostsByFollowerId(followerId, pageable);
	    return posts.map(post -> {
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
	                });
	}

	
	public Page<PostDTO> getNonFollowedAndNonReportedPosts(Long userId, Pageable pageable) {
	    Page<PostEntity> posts = postRepository.findNonFollowedAndNonReportedPosts(userId, pageable);
	 
	    return posts.map(post -> {
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
	                profiles != null ? profiles.get(0).getProfile_img() : null, // 프로필 이미지 추가
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
	    });
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
