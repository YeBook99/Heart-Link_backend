package com.ss.heartlinkapi.aspect;

import com.ss.heartlinkapi.login.dto.CustomUserDetails;
import com.ss.heartlinkapi.notification.dto.NotificationLikeDTO;
import com.ss.heartlinkapi.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class NotificationAspect {

    private final NotificationService notificationService;

    @AfterReturning("execution(* com.ss.heartlinkapi.report.service.ReportService.getAllList())")
    public void notifyLike(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        notificationService.notifyLike(authentication.getName(), 1L);
    }

    @AfterReturning("execution(* com.ss.heartlinkapi.message.service.MessageService.getAllChatMessage(..))")
    public void notifyComment(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName());
        notificationService.notifyComment(authentication.getName(),5L, 1L);
    }

    @AfterReturning("execution(* com.ss.heartlinkapi.message.service.MessageRoomService.getAllChatList(..))")
    public void notifyFollow(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName());
        notificationService.notifyFollow(authentication.getName(), 4L);
    }

}
