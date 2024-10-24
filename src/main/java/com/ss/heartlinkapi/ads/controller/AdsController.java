package com.ss.heartlinkapi.ads.controller;

import com.ss.heartlinkapi.ads.service.AdsService;
import com.ss.heartlinkapi.search.entity.SearchHistoryEntity;
import com.ss.heartlinkapi.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdsController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private AdsService adsService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserAds(@PathVariable Long userId){
        List<SearchHistoryEntity> history = searchService.findHistoryByUserId();

        if(history.isEmpty()){
            return null;
//            검색기록이 없을 때
        }

    }


}
