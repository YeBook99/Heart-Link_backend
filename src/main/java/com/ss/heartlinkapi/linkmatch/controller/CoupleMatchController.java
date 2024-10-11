package com.ss.heartlinkapi.linkmatch.controller;

import com.ss.heartlinkapi.linkmatch.dto.MatchAnswer;
import com.ss.heartlinkapi.linkmatch.service.CoupleMatchService;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchAnswerEntity;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/couple")
public class CoupleMatchController {

    @Autowired
    private CoupleMatchService coupleMatchService;

    // 커플 매치 질문 조회
    @GetMapping("/missionmatch/questions")
    public ResponseEntity<?> getMatchQuestion() {
        try {
            LinkMatchEntity result = coupleMatchService.getMatchQuestion();

            if (result != null) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 커플 매치 답변 저장
    @PostMapping("/missionmatch/questions/choose")
    public ResponseEntity<?> matchChoose(@RequestBody MatchAnswer matchAnswer) {

        try {
            if (matchAnswer == null || matchAnswer.getQuestionId() == null
                    || matchAnswer.getSelectedOption() > 1 || matchAnswer.getSelectedOption() < 0 || matchAnswer.getUserId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            LinkMatchAnswerEntity result = coupleMatchService.answerSave(matchAnswer);
            if (result != null) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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