package com.ss.heartlinkapi.mission.service;

import com.ss.heartlinkapi.contentLinktag.entity.ContentLinktagEntity;
import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.couple.repository.CoupleRepository;
import com.ss.heartlinkapi.linktag.entity.LinkTagEntity;
import com.ss.heartlinkapi.linktag.repository.LinkTagRepository;
import com.ss.heartlinkapi.mission.dto.LinkMissionDTO;
import com.ss.heartlinkapi.mission.entity.LinkMissionEntity;
import com.ss.heartlinkapi.mission.entity.UserLinkMissionEntity;
import com.ss.heartlinkapi.mission.repository.CoupleMissionRepository;
import com.ss.heartlinkapi.mission.repository.UserLinkMissionRepository;
import com.ss.heartlinkapi.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private UserLinkMissionRepository userLinkMissionRepository;

    @Autowired
    private CoupleRepository coupleRepository;

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
    public void checkMissionTag(LocalDateTime postDate, List<ContentLinktagEntity> postTag){
        // 지금 올린 피드의 날짜와 연결_링크태그 엔티티를 조회해온다.
        if(postTag != null) {
            // 연결_링크태그 테이블을 타고 링크 태그 테이블에서 그 태그들을 조회해온다.
            List<LinkTagEntity> tagList = new ArrayList<>();
            for (ContentLinktagEntity linkTagEntity : postTag) {
                LinkTagEntity tag = linkTagRepository.findById(linkTagEntity.getLinktagId().getId()).orElse(null);
                tagList.add(tag);
            }

            // 미션 테이블에서 이번달 태그 리스트를 가져온다.
            int year = postDate.getYear();
            int month = postDate.getMonthValue();
            List<LinkMissionEntity> missionList = missionRepository.findMissionByYearMonth(year, month);

            // 조회해 온 태그들의 아이디 중에서 이번 달의 미션 테이블의 태그 아이디와 동일한지 확인
            LinkMissionEntity findMission = null;
            for (LinkTagEntity tagItem : tagList) {
                for (LinkMissionEntity missionItem : missionList) {
                    if (tagItem.getId() == missionItem.getLinkTagId().getId()) {
                        findMission = missionItem;
                    }
                }
            }

            if (findMission == null) {
                // 미션 실패
            } else {
                // 미션 성공
                // 유저 아이디로 해당된 커플을 조회해옴.
                CoupleEntity couple = coupleRepository.findCoupleByUserId(postTag.get(0).getBoardId().getUserId().getUserId());

                // 유저 링크 미션 테이블에 추가
                UserLinkMissionEntity linkMission = new UserLinkMissionEntity();
                linkMission.setCoupleId(couple);
                linkMission.setLinkMissionId(findMission);
                linkMission.setStatus(true);
                UserLinkMissionEntity missionOk = userLinkMissionRepository.save(linkMission);
                if (missionOk != null) {
                    System.out.println("링크 미션 성공");
                } else {
                    System.out.println("링크 미션 실패");
                }
            }
        } else {
            System.out.println("설정한 태그가 없습니다.");
        }

    }

}
