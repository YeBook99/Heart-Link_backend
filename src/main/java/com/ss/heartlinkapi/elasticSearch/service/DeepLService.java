package com.ss.heartlinkapi.elasticSearch.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeepLService {

    private static final String DEEPL_API_URL = "https://api-free.deepl.com/v2/translate";
    private static final String AUTH_KEY = "04effbda-ae8c-4ce6-b384-78cc496fd41a:fx";

    public String translate(String text) {
        System.out.println("번역서비스1"+text);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "DeepL-Auth-Key "+AUTH_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("text", new String[]{text});
        body.put("target_lang", "EN");
        body.put("source_lang", "KO");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        System.out.println("번역서비스2"+entity);

        ResponseEntity<String> response = restTemplate.exchange(DEEPL_API_URL, HttpMethod.POST, entity, String.class);
        System.out.println("번역서비스3"+response);

        String result = response.getBody();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(result);
            System.out.println("뭐임진짜: " + jsonNode.get("translations").get(0).get("text").asText());
            return jsonNode.get("translations").get(0).get("text").asText(); // 번역된 텍스트만 반환
        } catch (Exception e) {
            e.printStackTrace();
            return "Translation error"; // 오류 처리
        }

    }

}
