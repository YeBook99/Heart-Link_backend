package com.ss.heartlinkapi.elasticSearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch.indices.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ElasticIndexService {

    private final ElasticsearchClient elasticsearchClient;
    private static final String INDEX_NAME = "search_history";
    private static final String MAPPING_FILE_PATH = "src/main/resources/elasticSearch/search_history_mapping.json";
    private static final String USER_INDEX_NAME = "user_info";
    private static final String TAG_INDEX_NAME = "tag_info";
    private static final String TAG_MAPPING_PATH = "src/main/resources/elasticSearch/tag_info_mapping.json";


    // 검색기록 인덱스 생성
    @PostConstruct
    public void initializeIndex(){
        try{
            if(!indexExists(INDEX_NAME)){
                // 인덱스가 존재하지 않을 경우
                createIndex(INDEX_NAME, MAPPING_FILE_PATH);
                System.out.println("엘라스틱 서치 인덱스 생성 완료. 인덱스 이름 : "+INDEX_NAME);
            } else {
                // 인덱스가 이미 존재할 경우
                System.out.println("엘라스틱 서치 인덱스가 이미 존재함. 인덱스 이름 : "+INDEX_NAME);
            }

            if(!indexExists(USER_INDEX_NAME)){
                // 인덱스가 존재하지 않을 경우
                createIdIndex();
                System.out.println("엘라스틱 서치 인덱스 생성 완료. 인덱스 이름 : "+USER_INDEX_NAME);
            } else {
                // 인덱스가 이미 존재할 경우
//                deleteIndex(USER_INDEX_NAME);
//                createIdIndex(USER_INDEX_NAME, USER_MAPPING_PATH);
                System.out.println("엘라스틱 서치 인덱스가 이미 존재함. 인덱스 이름 : "+USER_INDEX_NAME);
            }

            if(!indexExists(TAG_INDEX_NAME)){
                // 인덱스가 존재하지 않을 경우
                createIndex(TAG_INDEX_NAME, TAG_MAPPING_PATH);
                System.out.println("엘라스틱 서치 인덱스 생성 완료. 인덱스 이름 : "+TAG_INDEX_NAME);
            } else {
                // 인덱스가 이미 존재할 경우
                System.out.println("엘라스틱 서치 인덱스가 이미 존재함. 인덱스 이름 : "+TAG_INDEX_NAME);
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("엘라스틱서치 인덱스 생성 실패");
        }
    }

    // 유저 인덱스 생성
    public void createIdIndex() throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:9200/user_info";
        String requestBody = "{\n" +
                "  \"settings\": {\n" +
                "    \"analysis\": {\n" +
                "      \"normalizer\": {\n" +
                "        \"my_normalizer\": {\n" +
                "          \"type\": \"custom\",\n" +
                "          \"filter\": [\"lowercase\"]\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"mappings\": {\n" +
                "    \"properties\": {\n" +
                "      \"userId\": {\n" +
                "        \"type\": \"long\"\n" +
                "      },\n" +
                "      \"loginId\": {\n" +
                "        \"type\": \"keyword\",\n" +
                "        \"normalizer\": \"my_normalizer\"\n" +
                "      },\n" +
                "      \"name\": {\n" +
                "        \"type\": \"text\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.put(url, requestEntity);
        } catch (HttpClientErrorException e) {
            e.printStackTrace(); // 에러 로그 출력
        }
    }

    // 엘라스틱 서치에서 이미 해당 인덱스가 존재하는지 체크
    private boolean indexExists(String indexName) throws Exception{
        return elasticsearchClient.indices().exists(ExistsRequest.of(e->e.index(indexName))).value();
    }

    // 인덱스 삭제
    private void deleteIndex(String indexName) throws Exception {
        elasticsearchClient.indices().delete(DeleteIndexRequest.of(d -> d.index(indexName)));
        System.out.println("엘라스틱 서치 인덱스 삭제 완료. 인덱스 이름: " + indexName);
    }

    // 인덱스 생성
    private void createIndex(String indexName, String mappingFilePath) throws Exception{
        String mappingJson = new String(Files.readAllBytes(Paths.get(mappingFilePath)));

        // JSON 문자열을 Map으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> mapping = objectMapper.readValue(mappingJson, Map.class);

        // mappings의 properties 부분을 가져오기
        Map<String, Object> properties = (Map<String, Object>) ((Map<String, Object>) mapping.get("mappings")).get("properties");
        Map<String, Object> settings = (Map<String, Object>) mapping.getOrDefault("settings", new HashMap<>());

        // Map<String, Object> -> Map<String, Property> 변환
        Map<String, Property> propertyMap = convertToProperties(properties);

        // Elasticsearch 클라이언트에 인덱스 생성 요청
        elasticsearchClient.indices().create(CreateIndexRequest.of(c ->
                c.index(indexName)
                        .settings(s -> s // settings를 Map으로 변환
                                .numberOfShards(settings.getOrDefault("number_of_shards", "1").toString())
                                .numberOfReplicas(settings.getOrDefault("number_of_replicas", "0").toString())
                        )
                        .mappings(m -> m.properties(propertyMap))
        ));
    }

    // Object -> Property 타입 변환
    private Map<String, Property> convertToProperties(Map<String, Object> properties) {
        Map<String, Property> propertyMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String propertyName = entry.getKey();
            Map<String, Object> propertyDetails = (Map<String, Object>) entry.getValue();
            if(createProperty(propertyDetails) != null){
                propertyMap.put(propertyName, createProperty(propertyDetails));
            }
        }
        return propertyMap;
    }

    // Property 생성
    private Property createProperty(Map<String, Object> propertyDetails) {
        String type = propertyDetails.get("type").toString();
        switch (type) {
            case "keyword":
                return Property.of(p -> p.keyword(k -> k));
            case "text":
                return Property.of(p -> p.text(t -> t));
            case "date":
                return Property.of(p -> p.date(d -> d));
            case "integer":
                return Property.of(p -> p.integer(i -> i));
            case "float":
                return Property.of(p -> p.float_(f -> f));
            case "long":
                return Property.of(p -> p.long_(l -> l));
            case "boolean":
                return Property.of(p -> p.boolean_(b -> b));
            default:
                throw new IllegalArgumentException("Unsupported property type: " + type);
        }
    }

}
