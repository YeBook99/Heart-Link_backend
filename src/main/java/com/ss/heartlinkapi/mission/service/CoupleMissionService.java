package com.ss.heartlinkapi.mission.service;

import com.ss.heartlinkapi.mission.dto.LinkMissionDTO;
import com.ss.heartlinkapi.mission.entity.LinkMissionEntity;
import com.ss.heartlinkapi.mission.repository.CoupleMissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoupleMissionService {

    @Autowired
    private CoupleMissionRepository missionRepository;

    // 매월 미션 태그 리스트 조회
    public List<LinkMissionEntity> findMissionByYearMonth(Integer year, Integer month) {
        return missionRepository.findMissionByYearMonth(year, month);
    }
}
