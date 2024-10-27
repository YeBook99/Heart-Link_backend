package com.ss.heartlinkapi.ads.controller;

import com.ss.heartlinkapi.ads.service.AdsService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/get")
    public ResponseEntity<?> getAds(@RequestParam Long userId){
        List<Map<String, Object>> adsResult = adsService.getAds(userId);
        return ResponseEntity.ok(adsResult);
    }
}
