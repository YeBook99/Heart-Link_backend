package com.ss.heartlinkapi.like.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.comment.entity.CommentEntity;
import com.ss.heartlinkapi.comment.repository.CommentRepository;
import com.ss.heartlinkapi.like.dto.LikeDTO;
import com.ss.heartlinkapi.like.entity.LikeEntity;
import com.ss.heartlinkapi.like.repository.LikeRepository;
import com.ss.heartlinkapi.post.dto.PostFileDTO;
import com.ss.heartlinkapi.post.entity.PostEntity;
import com.ss.heartlinkapi.post.entity.PostFileEntity;
import com.ss.heartlinkapi.post.repository.PostRepository;
import com.ss.heartlinkapi.user.entity.ProfileEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;
import com.ss.heartlinkapi.user.repository.ProfileRepository;
import com.ss.heartlinkapi.user.repository.UserRepository;

import co.elastic.clients.elasticsearch.security.get_token.UserRealm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LikeService {
	
	private final LikeRepository likeRepository;
	private final ProfileRepository profileRepository;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	
	public LikeService(LikeRepository likeRepository, ProfileRepository profileRepository, PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository) {
		this.likeRepository = likeRepository;
		this.profileRepository = profileRepository;
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
		this.userRepository = userRepository;
	}
	
	// 게시글 좋아요 목록 조회
	@Transactional
	public List<LikeDTO> getLikesByPostId(Long postId) {
		List<LikeEntity> likes = likeRepository.findByPostId_PostId(postId);
		
		
		return likes.stream()
                .map(like -> {
                    
                    List<ProfileEntity> profiles = profileRepository.findAllByUserEntity(like.getUserId());

                    return new LikeDTO(
                        like.getLikeId(),
                        like.getUserId().getUserId(),
                        like.getUserId().getLoginId(),
                        (profiles != null) ? profiles.get(0).getProfile_img() : null, // 프로필 이미지 추가
                        postId,
                        null,
                        like.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());
    }

	// 댓글 좋아요 목록 조회
	public List<LikeDTO> getLikesByCommentId(Long commentId) {

		List<LikeEntity> likes = likeRepository.findByPostId_PostId(commentId);
			
			
		return likes.stream()
		            .map(like -> {
		                
		                List<ProfileEntity> profiles = profileRepository.findAllByUserEntity(like.getUserId());
		
		                return new LikeDTO(
		                    like.getLikeId(),
		                    like.getUserId().getUserId(),
		                    like.getUserId().getLoginId(),
		                    (profiles != null) ? profiles.get(0).getProfile_img() : null, // 프로필 이미지 추가
		                    null,
		                    commentId,
		                    like.getCreatedAt()
		                );
		            })
		            .collect(Collectors.toList());
	}
	
	// 내가 누른 좋아요 목록 조회
	public List<PostFileDTO> getPostFilesByUserId(Long userId){
		List<PostFileEntity> postFiles = likeRepository.findLikePostFilesByUserId(userId);
		
		return postFiles.stream()
				.map(file -> new PostFileDTO(
								file.getPostId().getPostId(),
								file.getFileUrl(),
								file.getFileType(),
								file.getSortOrder()
								))
				.collect(Collectors.toList());

	}
	
	// 게시글 좋아요 추가, 삭제
	@Transactional
	public boolean addOrRemoveLike(Long postId, Long userId, Long commentId) {
	    UserEntity user = userRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("User not found"));
	    
	    // 게시글 또는 댓글 엔티티 가져오기
	    PostEntity post = null;
	    CommentEntity comment = null;
	    if (postId != null) {
	        post = postRepository.findById(postId)
	                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
	    } else if (commentId != null) {
	        comment = commentRepository.findById(commentId)
	                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
	    } else {
	        throw new IllegalArgumentException("Either postId or commentId must be provided");
	    }

	    // 중복 좋아요 여부 확인
	    Optional<LikeEntity> existingLike;
	    if (post != null) {
	        existingLike = likeRepository.findByUserIdAndPostId(user, post);
	    } else {
	        existingLike = likeRepository.findByUserIdAndCommentId(user, comment);
	    }

	    if (existingLike.isPresent()) {
	        // 좋아요가 이미 있으면 삭제
	        likeRepository.delete(existingLike.get());
	        if (post != null) {
	            post.setLikeCount(post.getLikeCount() - 1);
	            postRepository.save(post);
	        } else {
	            comment.setLikeCount(comment.getLikeCount() - 1);
	            commentRepository.save(comment);
	        }
	    } else {
	        // 좋아요가 없으면 추가
	        LikeEntity like = new LikeEntity();
	        like.setUserId(user);
	        if (post != null) {
	            like.setPostId(post);
	            post.setLikeCount(post.getLikeCount() + 1);
	            postRepository.save(post);
	        } else {
	            like.setCommentId(comment);
	            comment.setLikeCount(comment.getLikeCount() + 1);
	            commentRepository.save(comment);
	        }
	        likeRepository.save(like);
	    }
	    return true;
	}

				


}
