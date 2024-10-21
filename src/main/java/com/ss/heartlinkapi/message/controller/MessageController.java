package com.ss.heartlinkapi.message.controller;

import com.ss.heartlinkapi.message.dto.ChatMsgListDTO;
import com.ss.heartlinkapi.message.dto.ChatUserDTO;
import com.ss.heartlinkapi.message.entity.MessageEntity;
import com.ss.heartlinkapi.message.service.MessageRoomService;
import com.ss.heartlinkapi.message.service.MessageService;
import com.ss.heartlinkapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dm")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageRoomService messageRoomService;
    private final MessageService messageService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<ChatUserDTO>> getAllChatList(@PathVariable("userId") Long userId) {
        List<ChatUserDTO> list = new ArrayList<>();
        list = messageRoomService.getAllChatList(userId);
        log.info("접근함");
        return ResponseEntity.ok(list);
    }
    @GetMapping("/{msgRoomId}/detail")
    public ResponseEntity<List<ChatMsgListDTO>> getAllChatMessage(@PathVariable("msgRoomId") Long msgRoomId){
        List<ChatMsgListDTO> list = new ArrayList<>();
        list = messageService.getAllChatMessage(msgRoomId);

        return ResponseEntity.ok(list);
    }
}
