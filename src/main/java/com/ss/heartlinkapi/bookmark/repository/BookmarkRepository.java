package com.ss.heartlinkapi.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.bookmark.entity.BookmarkEntity;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long>{

}
