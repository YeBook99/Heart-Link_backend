package com.ss.heartlinkapi.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.post.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Long>{

}
