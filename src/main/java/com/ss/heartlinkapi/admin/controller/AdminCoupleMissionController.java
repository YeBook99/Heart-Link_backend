package com.ss.heartlinkapi.admin.controller;

import com.ss.heartlinkapi.admin.service.AdminCoupleMissionService;
import com.ss.heartlinkapi.linktag.entity.LinkTagEntity;
import com.ss.heartlinkapi.linktag.repository.LinkTagRepository;
import com.ss.heartlinkapi.mission.dto.LinkMissionDTO;
import com.ss.heartlinkapi.mission.entity.LinkMissionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminCoupleMissionController {

    //임시 나중에 서비스 생기면 서비스껄로 가져오기
    @Autowired
    private LinkTagRepository linkTagRepository;

    @Autowired
    private AdminCoupleMissionService adminMissionService;

    @PostMapping("/missionLink")
    public ResponseEntity<?> addMissionTag(@RequestBody LinkMissionDTO missionTag) {
        try{

            if(missionTag == null || missionTag.getMissionTagName().isEmpty() || missionTag.getMissionEndDate()==null || missionTag.getMissionStartDate()==null){
                return ResponseEntity.badRequest().build();
            }

            LinkTagEntity result = linkTagRepository.findByKeywordContains(missionTag.getMissionTagName());

            if (result != null){
                // 기존 태그를 가져와서 넣기
                LinkMissionEntity addResult = adminMissionService.addMissionTag(result, missionTag);
                if(addResult == null){
                    return ResponseEntity.badRequest().build();
                } else {
                    return ResponseEntity.ok(addResult);
                }
            } else {
                // 태그 명 새로 만들기
                LinkTagEntity linkTagEntity = new LinkTagEntity();
                linkTagEntity.setKeyword(missionTag.getMissionTagName());
                LinkTagEntity addTagResult = linkTagRepository.save(linkTagEntity);
                LinkMissionEntity addResult = adminMissionService.addMissionTag(addTagResult, missionTag);
                if(addResult == null){
                    return ResponseEntity.badRequest().build();
                } else {
                    return ResponseEntity.ok(addResult);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
