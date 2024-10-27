package com.ss.heartlinkapi.ads.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.heartlinkapi.elasticSearch.document.SearchHistoryDocument;
import com.ss.heartlinkapi.elasticSearch.service.DeepLService;
import com.ss.heartlinkapi.elasticSearch.service.ElasticService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class AdsService {

    private final ElasticService elasticService;
    private final RestTemplate restTemplate;
    private HttpHeaders headers;

    private String token;

    public AdsService(ElasticService elasticService, DeepLService deepLService) {
        this.elasticService = elasticService;
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
    }

    // 회원 아이디로 광고 가져오기
    public List<Map<String, Object>> getAds(Long userId) {

        List<SearchHistoryDocument> searchList = elasticService.findByUserId(userId);
        List<String> historyList = new ArrayList<>();

        for (SearchHistoryDocument history : searchList) {
            String[] keywords = history.getKeyword().split(" "); // 키워드 쪼개기
            historyList.addAll(Arrays.asList(keywords)); // 쪼갠 키워드를 리스트에 추가
        }

        System.out.println("ads의 서비스의 searchList 쪼개고 난 뒤 : "+historyList);

        getAdsList(historyList);

        return null;

    }

    // 키워드를 넘겨서 광고 상품 목록 받아오기
    private Map<String, Object> getAdsList(List<String> keywords){
        final String EBAY_GET_URL = "https://svcs.ebay.com/services/search/FindingService/v1";
        final String APPKEY = "-HeartLin-PRD-7b15e11a8-28f07714";
        int getItemCount = 3; // 가져올 아이템 갯수


        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-EBAY-SOA-OPERATION-NAME", "findItemsByKeywords");
        headers.set("X-EBAY-SOA-SECURITY-APPNAME", APPKEY);
        headers.set("X-EBAY-SOA-REST-PAYLOAD", "true");
        headers.set("X-EBAY-SOA-RESPONSE-DATA-FORMAT", "JSON");

        String url = String.format(
                "%s?OPERATION-NAME=findItemsByKeywords&SECURITY-APPNAME=%s&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD=true&keywords=%s&paginationInput.entriesPerPage="+getItemCount+"&paginationInput.pageNumber=1",
                EBAY_GET_URL, APPKEY, keywords.get(0)
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        String result = response.getBody();
        System.out.println("광고 리스트 결과 : "+result);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(result);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    // 이베이 토큰 생성 (배치 프로그램으로 1시간 단위로 계속 발급)
    @PostConstruct
    public void getAdsToken() {
        final String EBAY_GETTOKEN_URL = "https://api.ebay.com/identity/v1/oauth2/token";
        final String EBAY_GETTOKEN_APPKEY = "LUhlYXJ0TGluLVBSRC03YjE1ZTExYTgtMjhmMDc3MTQ6UFJELWIxNWUxMWE4YWJjYS0zNWNhLTQ2MzYtOGI5NC02NDZi";

        headers = new HttpHeaders();
        headers.set("Authorization", "Basic "+EBAY_GETTOKEN_APPKEY);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // 변경된 콘텐츠 타입

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", "https://api.ebay.com/oauth/api_scope");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(EBAY_GETTOKEN_URL, HttpMethod.POST, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        token = jsonNode.get("access_token").asText();
        System.out.println("이베이 토큰 발급 : "+token);
    }
}
