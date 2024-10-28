package com.ss.heartlinkapi.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.heartlinkapi.post.entity.PostFileEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;

public interface PostFileRepository extends JpaRepository<PostFileEntity, Long>{
	
	// 게시글 첨부파일 가져오기
	@Query("SELECT pf FROM PostFileEntity pf WHERE pf.postId.id = :postId")
	List<PostFileEntity> findByPostId(@Param("postId") Long postId);

	
	// 사용자와 사용자의 커플 게시글 목록 가져오기
	@Query("SELECT pf FROM PostFileEntity pf JOIN PostEntity p ON pf.postId = p.postId " +
	           "WHERE pf.sortOrder = 1 AND p.userId.userId = :userId " +
	           "ORDER BY p.createdAt DESC")
	List<PostFileEntity> findPostFilesByUserId(@Param("userId") Long userId);




}
