package com.ss.heartlinkapi.elasticSearch.controller;

import com.ss.heartlinkapi.elasticSearch.document.ElasticUserDocument;
import com.ss.heartlinkapi.elasticSearch.document.SearchHistoryDocument;
import com.ss.heartlinkapi.elasticSearch.service.ElasticService;
import com.ss.heartlinkapi.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.POST;

import java.util.List;


@RestController
@RequestMapping("/es")
public class ElasticRestController {

    @Autowired
    private ElasticService elasticService;

    // 테스트 조회
    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestParam Long userId){
        List<SearchHistoryDocument> result = elasticService.findByUserId(userId);
        System.out.println(result);
        return ResponseEntity.ok(result);
    }

    // 테스트 유저 추가
    @PostMapping("/addUser")
    public ResponseEntity<?> testAddUser(@RequestBody UserEntity userEntity){
        ElasticUserDocument result = elasticService.addUser(userEntity);
        return ResponseEntity.ok(result);
    }
}
