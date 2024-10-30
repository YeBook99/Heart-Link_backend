package com.ss.heartlinkapi.elasticSearch.service;

import com.ss.heartlinkapi.elasticSearch.document.ElasticUserDocument;
import com.ss.heartlinkapi.elasticSearch.document.SearchHistoryDocument;
import com.ss.heartlinkapi.elasticSearch.repository.ElasticHistoryRepository;
import com.ss.heartlinkapi.elasticSearch.repository.ElasticUserInfoRepository;
import com.ss.heartlinkapi.search.entity.SearchHistoryEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticService {

    private final ElasticHistoryRepository elasticHistoryRepository;
    private final ElasticUserInfoRepository userInfoRepository;

    public ElasticService(ElasticHistoryRepository elasticHistoryRepository, ElasticUserInfoRepository userInfoRepository) {
        this.elasticHistoryRepository = elasticHistoryRepository;
        this.userInfoRepository = userInfoRepository;
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

    // 인덱스에서 유저 아이디로 최근 검색 순으로 검색기록 찾기
    public List<SearchHistoryDocument> findByUserId(Long userId) {
        List<SearchHistoryDocument> searchList = elasticHistoryRepository.findByUserIdOrderByDateDesc(userId);
        return searchList;
    }

    // 유저 추가
    public ElasticUserDocument addUser(UserEntity userEntity) {
        ElasticUserDocument elasticUserDocument = new ElasticUserDocument();
        elasticUserDocument.setUserId(userEntity.getUserId());
        elasticUserDocument.setLoginId(userEntity.getLoginId());
        elasticUserDocument.setName(userEntity.getName());
        return userInfoRepository.save(elasticUserDocument);
    }

}
