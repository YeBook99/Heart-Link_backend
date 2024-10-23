package com.ss.heartlinkapi.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticConnection {

    @Autowired
    private ElasticsearchClient elasticsearchClient;


}
