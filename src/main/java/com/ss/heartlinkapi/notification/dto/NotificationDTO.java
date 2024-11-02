package com.ss.heartlinkapi.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationDTO {
    private String otherUserImg;    //  상대방 이미지
    private String type;    // 알람의 유형
    private String message;     // 알람에 담긴 메시지
    private LocalDateTime createdAt;    //  알람이 생성된 시간
}
