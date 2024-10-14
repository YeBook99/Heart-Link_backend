package com.ss.heartlinkapi.admin.service;

import com.ss.heartlinkapi.linktag.entity.LinkTagEntity;
import com.ss.heartlinkapi.mission.dto.LinkMissionDTO;
import com.ss.heartlinkapi.mission.entity.LinkMissionEntity;
import com.ss.heartlinkapi.mission.repository.CoupleMissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminCoupleMissionService {

    @Autowired
    private CoupleMissionRepository missionRepository;

    // 링크 미션 태그 추가
    public LinkMissionEntity addMissionTag(LinkTagEntity linkTag, LinkMissionDTO linkMissionDTO) {
        LinkMissionEntity linkMissionEntity = new LinkMissionEntity();
        linkMissionEntity.setLinkTagId(linkTag);
        linkMissionEntity.setStart_date(linkMissionDTO.getMissionStartDate());
        linkMissionEntity.setEnd_date(linkMissionDTO.getMissionEndDate());
        LinkMissionEntity result = missionRepository.save(linkMissionEntity);
        return result;
    }
}
