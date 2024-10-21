package com.ss.heartlinkapi.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.comment.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long>{

}
