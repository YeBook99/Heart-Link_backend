package com.ss.heartlinkapi.notification.controller;

import com.ss.heartlinkapi.login.dto.CustomUserDetails;
import com.ss.heartlinkapi.notification.dto.NotificationDTO;
import com.ss.heartlinkapi.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

//    client가 SseEmitter를 넘겨받는 엔드포인트
    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable Long userId) {
        return notificationService.subscribe(userId);
    }


//    알림 목록을 불러오는 기능
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications(@AuthenticationPrincipal CustomUserDetails user) {
        List<NotificationDTO> notifications = notificationService.getNotifications(user);
        return ResponseEntity.ok(notifications);
    }
}
