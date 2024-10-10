package com.ss.heartlinkapi.couple.repository;

import com.ss.heartlinkapi.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoupleUserRepository extends JpaRepository<UserEntity, Long> {
}
