package com.ss.heartlinkapi.notification.controller;

import com.ss.heartlinkapi.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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

//    sse 전송 확인 테스트용 도메인
    @PostMapping("/send-data/{userId}")
    public void sendData(@PathVariable Long userId) {
        notificationService.notify(userId, "data");
    }

}
