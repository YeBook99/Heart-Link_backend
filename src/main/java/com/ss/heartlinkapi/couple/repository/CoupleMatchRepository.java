package com.ss.heartlinkapi.couple.repository;

import com.ss.heartlinkapi.linkmatch.entity.LinkMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoupleMatchRepository extends JpaRepository<LinkMatchEntity, Long> {
}
