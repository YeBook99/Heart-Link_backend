package com.ss.heartlinkapi.search.service;

import com.ss.heartlinkapi.linktag.entity.LinkTagEntity;
import com.ss.heartlinkapi.linktag.repository.LinkTagRepository;
import com.ss.heartlinkapi.post.entity.PostEntity;
import com.ss.heartlinkapi.post.repository.PostRepository;
import com.ss.heartlinkapi.search.entity.SearchHistoryEntity;
import com.ss.heartlinkapi.search.repository.SearchRepository;
import com.ss.heartlinkapi.user.entity.UserEntity;
import com.ss.heartlinkapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SearchService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LinkTagRepository linkTagRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SearchRepository searchRepository;

    // 유저 아이디 검색
    @Transactional
    public UserEntity searchByUserId(String keyword, Long userId) {
        keyword = keyword.trim().substring(1);

        UserEntity findUser = userRepository.findByLoginId(keyword);
        UserEntity user = userRepository.findById(userId).orElse(null);

        if(user == null || findUser == null) {
            return null;
        }

        SearchHistoryEntity searchHistory = searchRepository.findByKeywordAndTypeAndUserId(keyword, "id", user);

        if(searchHistory != null) {
            searchHistory.setUpdatedAt(LocalDateTime.now());
            searchRepository.save(searchHistory);
        } else {
            SearchHistoryEntity searchHistoryEntity = new SearchHistoryEntity();
            searchHistoryEntity.setUserId(user);
            searchHistoryEntity.setKeyword(user.getLoginId());
            searchHistoryEntity.setType("id");
            searchHistoryEntity.setCreatedAt(LocalDateTime.now());
            SearchHistoryEntity result = searchRepository.save(searchHistoryEntity);
            System.out.println("Result : " + result);
        }
        return findUser;
    }

    // 태그명으로 태그 검색
    public LinkTagEntity searchByTag(String keyword, Long userId) {
        keyword = keyword.trim().substring(1);

        LinkTagEntity findTag = linkTagRepository.findByKeywordContains(keyword);
        UserEntity user = userRepository.findById(userId).orElse(null);

        if(user == null || findTag == null) {
            return null;
        }

        SearchHistoryEntity searchHistory = searchRepository.findByKeywordAndTypeAndUserId(keyword, "tag", user);

        if(searchHistory != null) {
            searchHistory.setUpdatedAt(LocalDateTime.now());
            searchRepository.save(searchHistory);
        } else {
            SearchHistoryEntity searchHistoryEntity = new SearchHistoryEntity();
            searchHistoryEntity.setUserId(user);
            searchHistoryEntity.setKeyword(findTag.getKeyword());
            searchHistoryEntity.setType("tag");
            searchHistoryEntity.setCreatedAt(LocalDateTime.now());
            SearchHistoryEntity result = searchRepository.save(searchHistoryEntity);
            System.out.println("Result : " + result);
        }
        return findTag;
    }

    // 키워드로 게시글 검색
    public List<PostEntity> searchByPost(String keyword, Long userId) {
        keyword = keyword.trim();
        List<PostEntity> findPost = postRepository.findAllByContentIgnoreCaseContaining(keyword);
        UserEntity user = userRepository.findById(userId).orElse(null);

        if(user == null || findPost == null) {
            return null;
        }

        SearchHistoryEntity searchHistory = searchRepository.findByKeywordAndTypeAndUserId(keyword, "content", user);

        if(searchHistory != null) {
            searchHistory.setUpdatedAt(LocalDateTime.now());
            searchRepository.save(searchHistory);
        } else {
            SearchHistoryEntity searchHistoryEntity = new SearchHistoryEntity();
            searchHistoryEntity.setUserId(user);
            searchHistoryEntity.setKeyword(keyword);
            searchHistoryEntity.setType("content");
            searchHistoryEntity.setCreatedAt(LocalDateTime.now());
            SearchHistoryEntity result = searchRepository.save(searchHistoryEntity);
            System.out.println("Result : " + result);
        }
        return findPost;
    }

    // 유저 아이디로 검색기록 가져오기
    public List<Map<String, Object>> findHistoryByUserId(Long userId) {
       UserEntity user = userRepository.findById(userId).orElse(null);
       List<SearchHistoryEntity> history = searchRepository.findByUserId(user);
       if(history.isEmpty()) {
           return null;
       }
       List<Map<String, Object>> historyList = new ArrayList<>();
       int size = history.size()<=5 ? history.size() : 5;
       for(int i=0; i<size; i++) {
           Map<String, Object> map = new HashMap<>();
           map.put("type", history.get(i).getType());
           map.put("keyword", history.get(i).getKeyword());
           if(history.get(i).getUpdatedAt() != null) {
               map.put("date", history.get(i).getUpdatedAt());
           } else {
               map.put("date", history.get(i).getCreatedAt());
           }
           historyList.add(map);
       }

       return historyList;
    }
}
