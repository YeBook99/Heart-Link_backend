package com.ss.heartlinkapi.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.like.entity.LikeEntitiy;

public interface LikeRepository extends JpaRepository<LikeEntitiy, Long>{

}
