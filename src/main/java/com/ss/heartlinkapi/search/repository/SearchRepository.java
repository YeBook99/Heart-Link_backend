package com.ss.heartlinkapi.search.repository;

import com.ss.heartlinkapi.search.entity.SearchHistoryEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SearchRepository extends JpaRepository<SearchHistoryEntity, Long> {

    // 검색어, 타입, 아이디로 검색내역 조회
    SearchHistoryEntity findByKeywordAndTypeAndUserId(String keyword, String type, UserEntity userId);
}
