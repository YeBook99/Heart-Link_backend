package com.ss.heartlinkapi.linktag.repository;

import com.ss.heartlinkapi.linktag.entity.LinkTagEntity;
import com.ss.heartlinkapi.mission.dto.LinkMissionDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkTagRepository extends JpaRepository<LinkTagEntity, Long> {

    // 키워드명으로 검색
    LinkTagEntity findByKeywordContains(String missionTagName);
}
