package com.ss.heartlinkapi.mission.repository;

import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.mission.dto.UserLinkMissionDTO;
import com.ss.heartlinkapi.mission.entity.LinkMissionEntity;
import com.ss.heartlinkapi.mission.entity.UserLinkMissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserLinkMissionRepository extends JpaRepository<UserLinkMissionEntity, Long> {

    // 커플 아이디로 해당되는 미션 달성 목록 받기
    public List<UserLinkMissionEntity> findAllByCoupleId(CoupleEntity couple);

    // 커플아이디로 삭제하기
    public void deleteAllByCoupleId(Long coupleId);

    // 유저아이디로 완료된 커플 미션 조회
    
}