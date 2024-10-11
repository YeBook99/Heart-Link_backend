package com.ss.heartlinkapi.admin.controller;

import com.ss.heartlinkapi.admin.service.AdminCoupleService;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public void addMatchQuestion(@RequestBody LinkMatchEntity questionText) {
        adminCoupleService.addMatchQuestion(questionText);
    }

    // 매치 질문 수정
    @PutMapping("/questions/{questionId}/update")
    public void updateMatchQuestion(@PathVariable Long questionId, @RequestBody LinkMatchEntity questionText) {
        adminCoupleService.updateMatchQuestion(questionId, questionText);
    }

    // 매치 질문 삭제
    @DeleteMapping("/questions/{questionId}/delete")
    public void deleteMatchQuestion(@PathVariable Long questionId) {
        adminCoupleService.deleteMatchQuestion(questionId);
    }

}
