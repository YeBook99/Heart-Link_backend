package com.ss.heartlinkapi.notification.service;

import com.ss.heartlinkapi.notification.dto.NotificationCommentDTO;
import com.ss.heartlinkapi.notification.dto.NotificationFollowDTO;
import com.ss.heartlinkapi.notification.dto.NotificationLikeDTO;
import com.ss.heartlinkapi.notification.entity.NotificationEntity;
import com.ss.heartlinkapi.notification.entity.Type;
import com.ss.heartlinkapi.notification.repository.EmitterRepository;
import com.ss.heartlinkapi.notification.repository.NotificationRepository;
import com.ss.heartlinkapi.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    //    기본 타임아웃 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;

    //    구독시 연결 해제 방지를 위해 더미데이터를 보내 연결을 유지시킨다.
    public SseEmitter subscribe(Long userId) {

        SseEmitter emitter = createdEmitter(userId);
        sendToClient(userId, "EventStream Created, [userId=" + userId + "]");
        return emitter;
    }

    //    이벤트발생시 data가 notify 메서드를 통해 sendToClient으로 넘어가고 client측으로 출력되게 된다.
    public void notifyLike(String userName, Long postId) {
        NotificationLikeDTO notificationLikeDTO = new NotificationLikeDTO("http://localhost:9090/like" + postId, "http://localhost:9090/img/고먀미1.jpeg", userName + "님이 회원님의 글을 좋아합니다.");
//        포스트 아이디 기준으로 작성자 찾아서 userId에 넣을 것.
        sendToClient(4L, notificationLikeDTO);
    }

    public void notifyComment(String userName, Long postId, Long commentId) {
        NotificationCommentDTO notificationCommentDTO = new NotificationCommentDTO("http://localhost:9090/comments/" + postId, "http://localhost:9090/img/고먀미1.jpeg", commentId, userName + "님이 댓글을 남겼습니다.");
//        포스트 아이디 기준으로 작성자 찾아서 userId에 넣을 것.
        sendToClient(4L, notificationCommentDTO);
    }

    public void notifyFollow(String userName, Long userId) {
        NotificationFollowDTO notificationFollowDTO = new NotificationFollowDTO("http://localhost:9090/follow", userName + "님이 팔로우하였습니다.");
        sendToClient(userId, notificationFollowDTO);
    }

    //  실질적으로 client에게 메세지를 전달해주는 메서드
    private void sendToClient(Long userId, Object data) {

        SseEmitter emitter = emitterRepository.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().id(String.valueOf(userId)).name("sse").data(data));
            } catch (IOException e) {
                emitterRepository.deleteById(userId);
                emitter.completeWithError(e);
            }
        }

    }

    //      더미데이터를 생성하고 저장소에 넣어서 관리
    private SseEmitter createdEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);

//        emitter의 시간이 종료 되거나 이벤트가 완료되었을 시에 emitterRepository에서 userId를 기준으로 저장된 emitter들을 삭제한다.
        emitter.onCompletion(() -> emitterRepository.deleteById(userId));
        emitter.onTimeout(() -> emitterRepository.deleteById(userId));

        return emitter;
    }
}
