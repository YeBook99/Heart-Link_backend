package com.ss.heartlinkapi.ads.controller;

import com.ss.heartlinkapi.ads.service.AdsService;
import com.ss.heartlinkapi.login.dto.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ads")
public class AdsController {

    private final AdsService adsService;

    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    // 유저의 검색기록으로 관련 광고 받아오기
    @GetMapping("/get")
    public ResponseEntity<?> getAds(@AuthenticationPrincipal CustomUserDetails user){
        List<Map<String, Object>> adsResult = adsService.getAds(user.getUserId());
        return ResponseEntity.ok(adsResult);
    }

    @GetMapping("/pickGet")
    public ResponseEntity<?> getAds(){
        String url = "https://adpick.co.kr/apis/offers.php?affid=62b120&os=ios&adtype=CPA&category=game&order=rand";
        adsService.getPickAds(url);
        return ResponseEntity.ok().build();
    }
}
