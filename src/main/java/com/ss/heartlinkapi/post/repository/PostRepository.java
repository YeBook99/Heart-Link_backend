package com.ss.heartlinkapi.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.heartlinkapi.post.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Long>{

	// 내 팔로잉 게시물 조회
	@Query("SELECT p FROM PostEntity p " +
	           "JOIN FollowEntity f ON f.following.id = p.userId.userId " +
	           "WHERE f.follower.id = :followerId " +
	           "AND p.visibility = com.ss.heartlinkapi.post.entity.Visibility.PUBLIC " +
	           "AND NOT EXISTS (SELECT r FROM ReportEntity r WHERE r.postId.postId = p.postId) " +
	           "ORDER BY p.createdAt DESC")
	List<PostEntity> findPublicPostsByFollowerId(@Param("followerId") Long followerId);

	// 내 팔로잉이 아닌 사람의 게시글 조회
	@Query("SELECT p FROM PostEntity p " +
	           "WHERE p.userId.userId NOT IN (SELECT f.following.id FROM FollowEntity f WHERE f.follower.id = :userId) " +
	           "AND NOT EXISTS (SELECT r FROM ReportEntity r WHERE r.postId = p.postId) " +
	           "AND p.visibility = com.ss.heartlinkapi.post.entity.Visibility.PUBLIC " +
	           "ORDER BY p.createdAt DESC")
	    List<PostEntity> findNonFollowedAndNonReportedPosts(@Param("userId") Long userId);
	
	// 키워드가 포함된 게시글 내용 검색
	List<PostEntity> findAllByContentIgnoreCaseContaining(String keyword);

}
