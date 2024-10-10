package com.ss.heartlinkapi.couple.controller;

import com.ss.heartlinkapi.couple.service.CoupleMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/couple")
public class CoupleMatchController {

    @Autowired
    private CoupleMatchService coupleMatchService;

    // 커플 매치 질문 저장


    // 커플 매치 답변 저장
    @PostMapping("/missionmatch/questions/choose")
    public ResponseEntity<?> matchChoose(@RequestParam Long userId, @RequestParam Long questionId, @RequestParam int selectedOption) {
        System.out.println(userId);

        coupleMatchService.answerSave(userId, questionId, selectedOption);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}