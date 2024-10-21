package com.ss.heartlinkapi.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.heartlinkapi.post.entity.PostFileEntity;

public interface PostFileRepository extends JpaRepository<PostFileEntity, Long>{
	
	// 게시글 첨부파일 가져오기
	@Query("SELECT pf FROM PostFileEntity pf WHERE pf.postId.id = :postId")
	List<PostFileEntity> findByPostId(@Param("postId") Long postId);

}
