package com.ss.heartlinkapi.block.repository;

import com.ss.heartlinkapi.user.entity.UserEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.block.entity.BlockEntity;
import com.ss.heartlinkapi.couple.entity.CoupleEntity;

public interface BlockRepository extends JpaRepository<BlockEntity, Long>{
	
    /********* 차단한 유저와 차단된 유저로 블락 엔티티 반환 **********/
    BlockEntity findByBlockedIdAndBlockerId(UserEntity blockedId, UserEntity blockerId);
    
    /********* 차단한 유저와 차단된 유저로 블락 엔티티 존재 확인 **********/
    boolean existsByBlockerIdAndBlockedId(UserEntity blockedId, UserEntity blockerId);
    
    // 차단한 유저와 차단된 커플이 일치하는 차단이 존재하는지 확인
    boolean existsByBlockerIdAndCoupleId(UserEntity blocker, CoupleEntity couple);
    
    /********* 유저엔티티로 블락엔티티리스트 반환 **********/
    List<BlockEntity> findByBlockerId(UserEntity blocker);
    
}