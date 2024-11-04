package com.ss.heartlinkapi.search.controller;

import com.ss.heartlinkapi.linktag.entity.LinkTagEntity;
import com.ss.heartlinkapi.login.dto.CustomUserDetails;
import com.ss.heartlinkapi.post.entity.PostEntity;
import com.ss.heartlinkapi.search.service.SearchService;
import com.ss.heartlinkapi.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    // 유저 별 검색기록 확인
    @GetMapping("/history")
    public ResponseEntity<?> searchHistory(@AuthenticationPrincipal CustomUserDetails user){
        try{
            if(user == null){
                return ResponseEntity.badRequest().body("유저 아이디가 존재하지 않습니다.");
            }

            List<Map<String, Object>> historyList = searchService.findHistoryByUserId(user.getUserId());
            if(historyList == null){
                return ResponseEntity.ok().body("검색 기록이 없습니다.");
            }

            return ResponseEntity.ok().body(historyList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

    }

    // 유저, 태그, 피드 내용 별 검색 후 결과 반환
    @GetMapping("/keyword")
    public ResponseEntity<?> search(@RequestParam String keyword, @AuthenticationPrincipal CustomUserDetails user) {
        try {

            if(keyword == null || keyword.isEmpty() || user == null) {
                return ResponseEntity.badRequest().body(null);
            }

            if (keyword.startsWith("@")) {
                System.out.println(keyword);
                UserEntity userEntity = searchService.searchByUserId(keyword, user.getUserId());
                if(userEntity == null) {
                    return ResponseEntity.ok("검색 결과가 없습니다.");
                }
                return ResponseEntity.ok(userEntity.getUserId());
            } else if (keyword.startsWith("&")) {
                LinkTagEntity tag = searchService.searchByTag(keyword, user.getUserId());
                if(tag == null) {
                    return ResponseEntity.ok("검색 결과가 없습니다.");
                }
                return ResponseEntity.ok(tag.getId());
            } else {
                System.out.println("키워드 : "+keyword);
                List<PostEntity> post = searchService.searchByPost(keyword, user.getUserId());
                if(post == null) {
                    return ResponseEntity.ok("검색 결과가 없습니다.");
                }
                List<Map<String, Object>> postList = new ArrayList<>();
                for(int i=0; i<post.size(); i++) {
                    Map<String, Object> postMap = new HashMap<>();
                    for(int j=0; j<post.size(); j++) {
                        postMap.put("id", post.get(i).getPostId());
                        postMap.put("content", post.get(i).getContent());
                    }
                    postList.add(i, postMap);
                }

                return ResponseEntity.ok(postList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // 검색창과 함께 띄울 게시글 조회
    @GetMapping("/getSearchPost")
    public ResponseEntity<?> getPostList(@AuthenticationPrincipal CustomUserDetails user, @RequestParam(required = false) Integer cursor, @RequestParam(defaultValue = "30") int limit) {
        try {
            if(user == null) {
                return ResponseEntity.badRequest().body(null);
            }
            Map<String, Object> postList = searchService.getPost(user, cursor, limit);
            return ResponseEntity.ok(postList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // 언급 시 보여줄 아이디 리스트 (팔로우 우선+모든 아이디)
    @GetMapping("/mentionId")
    public ResponseEntity<?> mentionId(@AuthenticationPrincipal CustomUserDetails user) {
        try{
            if(user == null) {
                return ResponseEntity.badRequest().body(null);
            }

        List<Map<String, Object>> userList = searchService.mentionIdList(user.getUserEntity());
        return ResponseEntity.ok(userList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
