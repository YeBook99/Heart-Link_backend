package com.ss.heartlinkapi.couple.repository;

import com.ss.heartlinkapi.linkmatch.entity.LinkMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;

public interface CoupleMatchRepository extends JpaRepository<LinkMatchEntity, Long> {

    public LinkMatchEntity findByDisplayDate(LocalDate today);
}
