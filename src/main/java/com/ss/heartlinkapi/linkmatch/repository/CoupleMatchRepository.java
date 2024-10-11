package com.ss.heartlinkapi.linkmatch.repository;

import com.ss.heartlinkapi.linkmatch.entity.LinkMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface CoupleMatchRepository extends JpaRepository<LinkMatchEntity, Long> {

    // 오늘의 매치 질문 확인
    public LinkMatchEntity findByDisplayDate(LocalDate today);
}
