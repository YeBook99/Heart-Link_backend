package com.ss.heartlinkapi.mission.service;

import com.ss.heartlinkapi.linktag.entity.LinkTagEntity;
import com.ss.heartlinkapi.linktag.repository.LinkTagRepository;
import com.ss.heartlinkapi.mission.dto.LinkMissionDTO;
import com.ss.heartlinkapi.mission.entity.LinkMissionEntity;
import com.ss.heartlinkapi.mission.repository.CoupleMissionRepository;
import com.ss.heartlinkapi.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CoupleMissionService {

    @Autowired
    private CoupleMissionRepository missionRepository;

    @Autowired
    private LinkTagRepository linkTagRepository;

    @Autowired
    private PostRepository postRepository;

    // 매월 미션 태그 리스트 조회
    public List<LinkMissionEntity> findMissionByYearMonth(Integer year, Integer month) {
        return missionRepository.findMissionByYearMonth(year, month);
    }

    // 매월 미션 태그 리스트의 태그 객체 조회
    public List<Map<String, Object>> findMissionTag(List<LinkMissionEntity> missionList) {
        List<Map<String, Object>> tagList = new ArrayList<>();
        for (LinkMissionEntity missionEntity : missionList) {
            LinkTagEntity tag = linkTagRepository.findById(missionEntity.getLinkTagId().getId()).orElse(null);
            Map<String, Object> tagMap = new HashMap<>();
            tagMap.put("missionId", tag.getId());
            tagMap.put("linkTag", tag.getKeyword());
            tagList.add(tagMap);
        }
        return tagList;
    }

    // 이번 미션 태그에 달성되는지 확인하는 메서드
    public void checkMissionTag(){

    }

}
