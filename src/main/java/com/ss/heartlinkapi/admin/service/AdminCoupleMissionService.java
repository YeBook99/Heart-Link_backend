package com.ss.heartlinkapi.admin.service;

import com.ss.heartlinkapi.linktag.entity.LinkTagEntity;
import com.ss.heartlinkapi.linktag.repository.LinkTagRepository;
import com.ss.heartlinkapi.mission.dto.LinkMissionDTO;
import com.ss.heartlinkapi.mission.entity.LinkMissionEntity;
import com.ss.heartlinkapi.mission.repository.CoupleMissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminCoupleMissionService {

    @Autowired
    private CoupleMissionRepository missionRepository;

    // 임시. 나중에 서비스로 바꾸기
    @Autowired
    private LinkTagRepository linkTagRepository;

    // 링크 미션 태그 추가
    public LinkMissionEntity addMissionTag(LinkTagEntity linkTag, LinkMissionDTO linkMissionDTO) {
        LinkMissionEntity linkMissionEntity = new LinkMissionEntity();
        linkMissionEntity.setLinkTagId(linkTag);
        linkMissionEntity.setStart_date(linkMissionDTO.getMissionStartDate());
        linkMissionEntity.setEnd_date(linkMissionDTO.getMissionEndDate());
        LinkMissionEntity result = missionRepository.save(linkMissionEntity);
        return result;
    }

    // 미션 아이디로 미션 찾기
    public LinkMissionEntity findByMissionId(Long missionId) {
        LinkMissionEntity why = missionRepository.findById(missionId).orElse(null);
        return why;
    }

    // 미션 태그 수정
    public LinkMissionEntity updateMission(LinkMissionEntity beforeMission, LinkMissionDTO afterMission) {
        beforeMission.setStart_date(afterMission.getMissionStartDate());
        beforeMission.setEnd_date(afterMission.getMissionEndDate());

        LinkTagEntity findTag = linkTagRepository.findByKeywordContains(afterMission.getMissionTagName());

        if(findTag != null) {
            System.out.println("여긴가1");
            // 기존 태그가 존재할 때 기존 태그 사용
            beforeMission.setLinkTagId(findTag);
            return missionRepository.save(beforeMission);
        } else {
            System.out.println("여긴가2");
            // 기존에 태그가 존재하지 않을 때 새 태그 추가
            LinkTagEntity newTag = new LinkTagEntity();
            newTag.setKeyword(afterMission.getMissionTagName());
            linkTagRepository.save(newTag);
            System.out.println("여긴가3");
            beforeMission.setLinkTagId(newTag);
            return missionRepository.save(beforeMission);
        }
    }

    // 미션 태그 삭제
    public void deleteMissionTagById(LinkMissionEntity missionId) {
        missionRepository.delete(missionId);
    }
}
