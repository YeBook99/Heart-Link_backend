package com.ss.heartlinkapi.block.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.block.entity.BlockEntity;

public interface BlockRepository extends JpaRepository<BlockEntity, Long>{

}
