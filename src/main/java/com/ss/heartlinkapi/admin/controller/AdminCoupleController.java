package com.ss.heartlinkapi.admin.controller;

import com.ss.heartlinkapi.admin.service.AdminCoupleService;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminCoupleController {

    @Autowired
    private AdminCoupleService adminCoupleService;

    // 매치 질문 조회
    @GetMapping("/questions")
    public ResponseEntity<?> getAllquestions
    (@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        // 오류 500 검사
        try {
            Page<LinkMatchEntity> questions = adminCoupleService.findAllByOrderByIdDesc(page, size);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 매치 질문 등록
    @PostMapping("/questions")
    public ResponseEntity<?> addMatchQuestion(@RequestBody LinkMatchEntity questionText) {
        // 오류 500 검사
        try {
            LinkMatchEntity result = adminCoupleService.addMatchQuestion(questionText);

            // 오류 400 검사
            if (questionText == null || questionText.getMatch1() == null || questionText.getMatch2() == null
                    || questionText.getDisplayDate() == null || questionText.getLinkMatchId() == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 매치 질문 수정
    @PutMapping("/questions/{questionId}/update")
    public ResponseEntity<?> updateMatchQuestion(@PathVariable Long questionId, @RequestBody LinkMatchEntity questionText) {
        // 오류 500 검사
        try{
            LinkMatchEntity result = adminCoupleService.updateMatchQuestion(questionId, questionText);
            // 오류 400 검사
            if(questionText == null || questionText.getMatch1() == null || questionText.getMatch2() == null
            || questionText.getDisplayDate() == null || questionText.getLinkMatchId() == null) {
                return ResponseEntity.badRequest().build();
            }
            // 오류 404 검사
            if (result == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 매치 질문 삭제
    @DeleteMapping("/questions/{questionId}/delete")
    public ResponseEntity<?> deleteMatchQuestion(@PathVariable Long questionId) {
        // 오류 500 검사
        try{
            // 오류 400 검사
            if(questionId == null) {
                return ResponseEntity.badRequest().build();
            }

            LinkMatchEntity question = adminCoupleService.findById(questionId);

            // 오류 404 검사
            if (question == null) {
                return ResponseEntity.notFound().build();
            } else {
                adminCoupleService.deleteMatchQuestion(questionId);
                return ResponseEntity.ok().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
