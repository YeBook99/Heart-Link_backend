package com.ss.heartlinkapi.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ChatUserDTO {
    private Long msgRoomId;
    private String userName;
    private String userImg;
    private String lastMessage;
    private boolean isLogin;
}
