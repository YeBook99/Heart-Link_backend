package com.ss.heartlinkapi.ads.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@Service
public class AdsService {

    private final RestTemplate restTemplate;

    public AdsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getGoogleAds(){
        String url = "https://example.com/google-ads-endpoint";

        HttpHeaders headers = new HttpHeaders();



    }
}
