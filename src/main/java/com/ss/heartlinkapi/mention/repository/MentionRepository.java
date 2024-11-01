package com.ss.heartlinkapi.mention.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.mention.entity.MentionEntity;

public interface MentionRepository extends JpaRepository<MentionEntity, Long>{

}
