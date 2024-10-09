package com.ss.heartlinkapi.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{

}
