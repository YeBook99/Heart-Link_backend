package com.ss.heartlinkapi.couple.repository;

import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CoupleRepository extends JpaRepository<CoupleEntity, Long> {

    @Query("select c from CoupleEntity c where c.user1.userId = :userId or c.user2.userId = :userId")
    public CoupleEntity findCoupleByUserId(Long userId);
}
