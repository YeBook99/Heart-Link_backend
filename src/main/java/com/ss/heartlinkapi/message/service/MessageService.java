package com.ss.heartlinkapi.message.service;

import com.ss.heartlinkapi.message.dto.ChatMsgListDTO;
import com.ss.heartlinkapi.message.entity.MessageEntity;
import com.ss.heartlinkapi.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public List<ChatMsgListDTO> getAllChatMessage(Long msgRoomId) {

        List<ChatMsgListDTO> list = new ArrayList<>();

//
        List<MessageEntity> messageEntities = messageRepository.findByMsgRoomId(msgRoomId);

        for (MessageEntity entity : messageEntities) {

            Long senderId = entity.getSenderId();
            String content = entity.getContent();
            String emoji = entity.getEmoji();
            String imgUrl = "hello.jpg";
            LocalDateTime lastMessageTime = entity.getCreatedAt();
            boolean isRead = entity.isRead();

            ChatMsgListDTO chatMsgListDTO = ChatMsgListDTO.builder()
                    .senderId(senderId)
                    .content(content)
                    .emoji(emoji)
                    .lastMessageTime(lastMessageTime)
                    .imageUrl(imgUrl)
                    .isRead(isRead)
                    .build();

            list.add(chatMsgListDTO);
        }
        return list;
    }
}

