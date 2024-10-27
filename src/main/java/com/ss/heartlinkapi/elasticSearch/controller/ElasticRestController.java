package com.ss.heartlinkapi.elasticSearch.controller;

import com.ss.heartlinkapi.elasticSearch.document.SearchHistoryDocument;
import com.ss.heartlinkapi.elasticSearch.service.ElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
