package com.ss.heartlinkapi.elasticSearch.service;

import com.ss.heartlinkapi.elasticSearch.document.SearchHistoryDocument;
import com.ss.heartlinkapi.elasticSearch.repository.ElasticHistoryRepository;
import com.ss.heartlinkapi.search.entity.SearchHistoryEntity;
import org.springframework.stereotype.Service;

@Service
public class ElasticService {

    private final ElasticHistoryRepository elasticHistoryRepository;

    public ElasticService(ElasticHistoryRepository elasticHistoryRepository) {
        this.elasticHistoryRepository = elasticHistoryRepository;
    }

    // 검색기록 추가
    public SearchHistoryDocument addOrUpdateHistory(SearchHistoryEntity historyEntity) {
        System.out.println(historyEntity);
        SearchHistoryDocument historyDocument = findHistoryById(historyEntity.getSearchHistoryId());
        if (historyDocument == null) {
            // mysql의 검색기록이 인덱스 안에 없을 때
            historyDocument = new SearchHistoryDocument();
            historyDocument.setSearchHistoryId(historyEntity.getSearchHistoryId());
            historyDocument.setDate(historyEntity.getCreatedAt());
            historyDocument.setType(historyEntity.getType());
            historyDocument.setKeyword(historyEntity.getKeyword());
            historyDocument.setUserId(historyEntity.getUserId().getUserId());
            return elasticHistoryRepository.save(historyDocument);
        } else {
            // mysql의 검색기록이 인덱스 안에 있을 때
            historyDocument.setDate(historyEntity.getUpdatedAt());
            return elasticHistoryRepository.save(historyDocument);
        }
    }

    // mysql 테이블에 있는 검색기록이 도큐먼트에 있는지 확인
    private SearchHistoryDocument findHistoryById(Long historyEntityId) {
        return elasticHistoryRepository.findById(historyEntityId).orElse(null);
    }
}
