package com.ss.heartlinkapi.mission.service;

import com.ss.heartlinkapi.contentLinktag.entity.ContentLinktagEntity;
import com.ss.heartlinkapi.contentLinktag.repository.ContentLinktagRepository;
import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.couple.repository.CoupleRepository;
import com.ss.heartlinkapi.couple.service.CoupleService;
import com.ss.heartlinkapi.linktag.entity.LinkTagEntity;
import com.ss.heartlinkapi.linktag.repository.LinkTagRepository;
import com.ss.heartlinkapi.mission.entity.LinkMissionEntity;
import com.ss.heartlinkapi.mission.entity.UserLinkMissionEntity;
import com.ss.heartlinkapi.mission.repository.CoupleMissionRepository;
import com.ss.heartlinkapi.mission.repository.UserLinkMissionRepository;
import com.ss.heartlinkapi.post.dto.PostDTO;
import com.ss.heartlinkapi.post.entity.PostEntity;
import com.ss.heartlinkapi.post.entity.PostFileEntity;
import com.ss.heartlinkapi.post.repository.PostFileRepository;
import com.ss.heartlinkapi.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ContentLinktagRepository contentLinktagRepository;

    @Autowired
    private PostFileRepository postFileRepository;

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
    // 게시글 작성 기능 완성 후 반환값 확인 필요
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

    // 유저 미션 테이블에서 커플 아이디로 조회하기
    public List<UserLinkMissionEntity> findUserLinkMissionByCoupleId(CoupleEntity couple) {
        return userLinkMissionRepository.findAllByCoupleId(couple);
    }

    // 유저 미션 삭제
    public void deleteUserMissionByCoupleId(Long coupleId) {
        userLinkMissionRepository.deleteAllByCoupleId(coupleId);
    }

    // 모든 미션 리스트 조회
    public List<LinkMissionEntity> findAllMissions() {
        return missionRepository.findAll();
    }

    // 미션태그 아이디로 미션 태그 entity 조회
    public LinkMissionEntity findOneMissionTag(Long missionId) {
        return missionRepository.findById(missionId).orElse(null);
    }

    // 미션태그 아이디로 태그 entity 조회
    public LinkTagEntity findTagByMissionId(Long missionId) {
        LinkMissionEntity mission = missionRepository.findById(missionId).orElse(null);
        return linkTagRepository.findById(mission.getLinkTagId().getId()).orElse(null);
    }

    // 미션태그 아이디를 가지고 글 작성 페이지로 이동 시 태그 넘겨주기
    public PostDTO writePostWithTag(LinkTagEntity tag){
        PostDTO post = new PostDTO();
        post.setContent("&"+tag.getKeyword()+" ");
        return post;
    }

    // 유저 아이디로 완료된 미션태그 조회
    public List<Map<String, Object>> getMissionStatus(Long userId, Integer year, Integer month) {

        // 넘어온 날짜가 없을 경우 디폴트값 현재
        if(year == null){
            year = LocalDate.now().getYear();
        }
        if(month == null){
            month = LocalDate.now().getMonthValue();
        }

        // 유저아이디로 유저 게시글 작성시간을 기준으로 월로 조회해서 전부 가져오기
        List<PostEntity> postList = postRepository.findAllByUserIdAndMonth(userId, year, month);

        // 이번달의 태그 아이디들 조회해오기
        List<LinkMissionEntity> missionList = findMissionByYearMonth(year, month);

        // 유저 게시글들에 연결된 태그들 조회하기
        Set<Map<String, Object>> tagList = new HashSet<>();

        for(PostEntity postEntity : postList){
            // 유저가 작성한 게시글들의 아이디로 정보 조회
            List<ContentLinktagEntity> contentList = contentLinktagRepository.findByBoardId(postEntity);
            for(ContentLinktagEntity contentLinktagEntity : contentList){
                // 게시글들의 태그 아이디와 미션태그의 태그 아이디를 비교해서 맞으면 저장
                for(LinkMissionEntity mission : missionList){
                    if(contentLinktagEntity.getLinktagId()==mission.getLinkTagId()){
                        // 이번달의 태그와 이번달 작성한 게시글의 태그가 맞을 때
                        // 포스트 아이디로 포스트 이미지 조회
                        List<PostFileEntity> fileList = postFileRepository.findByPostId(contentLinktagEntity.getBoardId().getPostId());
                        Map<String, Object> map = new HashMap<>();
                        map.put("missionId", mission.getLinkMissionId());
                        map.put("tagId", mission.getLinkTagId().getId());
                        map.put("tagName", mission.getLinkTagId().getKeyword());
                        map.put("postId", contentLinktagEntity.getBoardId().getPostId());
                        map.put("postImgUrl", fileList.get(0).getFileUrl());
                        tagList.add(map);
                    }
                }
            }
        }

        if (tagList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> completeTag = new ArrayList<>(tagList);

        return completeTag;
    }
}
