package com.ss.heartlinkapi.search.controller;

import com.ss.heartlinkapi.linktag.entity.LinkTagEntity;
import com.ss.heartlinkapi.post.entity.PostEntity;
import com.ss.heartlinkapi.search.service.SearchService;
import com.ss.heartlinkapi.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/keyword")
    public ResponseEntity<?> search(@RequestParam String keyword, @RequestParam Long userId) {
        try {

            if(keyword == null || keyword.isEmpty() || userId == null) {
                return ResponseEntity.badRequest().body(null);
            }

            if (keyword.startsWith("@")) {
                System.out.println(keyword);
                UserEntity user = searchService.searchByUserId(keyword, userId);
                if(user == null) {
                    return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(user.getUserId());
            } else if (keyword.startsWith("&")) {
                LinkTagEntity tag = searchService.searchByTag(keyword, userId);
                if(tag == null) {
                    return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(tag.getId());
            } else {
                System.out.println("키워드 : "+keyword);
                PostEntity post = searchService.searchByPost(keyword, userId);
                if(post == null) {
                    return ResponseEntity.notFound().build();
                }
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
