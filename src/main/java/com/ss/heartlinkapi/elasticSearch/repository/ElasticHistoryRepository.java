package com.ss.heartlinkapi.elasticSearch.repository;

import com.ss.heartlinkapi.elasticSearch.document.SearchHistoryDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticHistoryRepository extends ElasticsearchRepository<SearchHistoryDocument, Long> {
}
