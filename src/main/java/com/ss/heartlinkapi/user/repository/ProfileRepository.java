package com.ss.heartlinkapi.user.repository;

import com.ss.heartlinkapi.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.user.entity.ProfileEntity;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long>{

    ProfileEntity findByUserEntity(UserEntity user);

}
