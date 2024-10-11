package com.ss.heartlinkapi.couple.repository;

import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CoupleRepository extends JpaRepository<CoupleEntity, Long> {

    // 유저 아이디로 커플 아이디 조회
    @Query("select c from CoupleEntity c where c.user1.userId = :userId or c.user2.userId = :userId")
    public CoupleEntity findCoupleByUserId(Long userId);
}
