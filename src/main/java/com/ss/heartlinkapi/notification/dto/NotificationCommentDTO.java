package com.ss.heartlinkapi.notification.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotificationCommentDTO {
    private String url;
    private String postImgUrl;
    private Long commentId;
    private String message;
}
