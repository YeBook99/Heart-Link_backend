package com.ss.heartlinkapi.bookmark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.heartlinkapi.bookmark.entity.BookmarkEntity;
import com.ss.heartlinkapi.post.entity.PostFileEntity;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long>{
	
	// 내가 누른 북마크 목록 조회
    @Query("SELECT pf " +
           "FROM BookmarkEntity b " +
           "JOIN b.postId p " +
           "JOIN PostFileEntity pf ON pf.postId = p AND pf.sortOrder = 1 " +
           "WHERE b.userId.id = :userId")
    List<PostFileEntity> findBokkmarkPostFilesByUserId(@Param("userId") Long userId);

}
