package com.ss.heartlinkapi.couple.controller;

import com.ss.heartlinkapi.couple.dto.MatchAnswer;
import com.ss.heartlinkapi.couple.repository.CoupleMatchAnswerRepository;
import com.ss.heartlinkapi.couple.repository.CoupleMatchRepository;
import com.ss.heartlinkapi.couple.service.CoupleMatchService;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchAnswerEntity;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/couple")
public class CoupleMatchController {

    @Autowired
    private CoupleMatchService coupleMatchService;

    // 커플 매치 질문 조회
    @GetMapping("/missionmatch/questions")
    public LinkMatchEntity getMatchQuestion() {
        LinkMatchEntity result = coupleMatchService.getMatchQuestion();
        if(result != null) {
            return result;
        } else {
            return null;
        }
    }

    // 커플 매치 답변 저장
    @PostMapping("/missionmatch/questions/choose")
    public ResponseEntity<?> matchChoose(@RequestBody MatchAnswer matchAnswer) {

        LinkMatchAnswerEntity result = coupleMatchService.answerSave(matchAnswer);
        if(result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    // 커플 매치 성공여부 확인 임시
//    @Autowired
//    private CoupleMatchAnswerRepository rep;
//    @GetMapping("/missionmatch/questionss")
//    public int getMatchcheck() {
//        int result = rep.checkTodayMatch(1L);
//        System.out.println(result);
//        return result;
//    }
}