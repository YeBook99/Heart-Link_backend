package com.ss.heartlinkapi.block.repository;

import com.ss.heartlinkapi.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.block.entity.BlockEntity;
import com.ss.heartlinkapi.couple.entity.CoupleEntity;

public interface BlockRepository extends JpaRepository<BlockEntity, Long>{

    BlockEntity findByBlockedIdAndBlockerId(UserEntity blockedId, UserEntity blockerId);
    
    // 차단한 유저와 차단된 커플이 일치하는 차단이 존재하는지 확인
    boolean existsByBlockerIdAndCoupleId(UserEntity blocker, CoupleEntity couple);

}
