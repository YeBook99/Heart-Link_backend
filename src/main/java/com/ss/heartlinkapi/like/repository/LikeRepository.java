package com.ss.heartlinkapi.like.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.heartlinkapi.comment.entity.CommentEntity;
import com.ss.heartlinkapi.like.entity.LikeEntity;
import com.ss.heartlinkapi.post.entity.PostEntity;
import com.ss.heartlinkapi.post.entity.PostFileEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
	
	// 게시글 좋아요 목록 조회
    List<LikeEntity> findByPostId_PostId(Long postId);
    
    // 댓글 좋아요 목록 조회
    List<LikeEntity> findByCommentId_CommentId(Long commentId);
    
    // 내가 누른 좋아요 목록 조회
    @Query("SELECT pf " +
           "FROM LikeEntity l " +
           "JOIN l.postId p " +
           "JOIN PostFileEntity pf ON pf.postId = p AND pf.sortOrder = 1 " +
           "WHERE l.userId.id = :userId")
    List<PostFileEntity> findLikePostFilesByUserId(@Param("userId") Long userId);
    
    
    // 게시글 좋아요 증감
	Optional<LikeEntity> findByUserIdAndPostId(UserEntity user, PostEntity post);

	// 댓글 좋아요 증감
	Optional<LikeEntity> findByUserIdAndCommentId(UserEntity user, CommentEntity comment);
    

}
