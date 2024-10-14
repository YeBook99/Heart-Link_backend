package com.ss.heartlinkapi.message.service;

import com.ss.heartlinkapi.message.dto.ChatUserDTO;
import com.ss.heartlinkapi.message.entity.MessageRoomEntity;
import com.ss.heartlinkapi.message.repository.MessageRepository;
import com.ss.heartlinkapi.message.repository.MessageRoomRepository;
import com.ss.heartlinkapi.user.entity.UserEntity;
import com.ss.heartlinkapi.user.repository.ProfileRepository;
import com.ss.heartlinkapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageRoomService {

    private final MessageRoomRepository messageRoomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public List<ChatUserDTO> getAllChatList(Long userId) {

        List<ChatUserDTO> list = new ArrayList<>();

        List<MessageRoomEntity> messageRoomEntities = messageRoomRepository.findByUser1IdOrUser2Id(userId, userId);
        log.info("entities : {}", messageRoomEntities);
        for (MessageRoomEntity entity : messageRoomEntities) {
//            대화 상대 유저이름
//        String userName = userRepository.findById(userId).get().getLoginId();
            String userName = "히히";
//            대화 상대 유저 이미지(나중에 jpa연결되면 구현할것.)
            String userImg = "hello.jpg";
            Long messageRoomId = entity.getId();
//            마지막 메시지 구하기
            String lastMessage = messageRepository.findByMsgRoomIdOrderByCreatedAt(messageRoomId);
//            로그인 상태 확인
            boolean isLogin = true;

            new ChatUserDTO();

            list.add(ChatUserDTO.builder()
                    .userName(userName)
                    .userImg(userImg)
                    .lastMessage(lastMessage)
                    .build());
        }



        return list;
    }
}
