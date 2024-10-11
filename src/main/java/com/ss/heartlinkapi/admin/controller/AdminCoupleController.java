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
    public Page<LinkMatchEntity> getAllquestions
            (@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {

        System.out.println(adminCoupleService.findAllByOrderByIdDesc(page, size).toString());
        return adminCoupleService.findAllByOrderByIdDesc(page, size);
    }

    // 매치 질문 등록
    @PostMapping("/questions")
    public ResponseEntity<?> addMatchQuestion(@RequestBody LinkMatchEntity questionText) {
        LinkMatchEntity result = adminCoupleService.addMatchQuestion(questionText);
        if(result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 매치 질문 수정
    @PutMapping("/questions/{questionId}/update")
    public ResponseEntity<?> updateMatchQuestion(@PathVariable Long questionId, @RequestBody LinkMatchEntity questionText) {
        LinkMatchEntity result = adminCoupleService.updateMatchQuestion(questionId, questionText);
        if(result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 매치 질문 삭제
    @DeleteMapping("/questions/{questionId}/delete")
    public ResponseEntity<?> deleteMatchQuestion(@PathVariable Long questionId) {
        LinkMatchEntity question = adminCoupleService.findById(questionId);
        if(question != null) {
            adminCoupleService.deleteMatchQuestion(questionId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
