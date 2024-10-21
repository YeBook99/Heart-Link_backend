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
import org.springframework.web.bind.annotation.*;

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

//    사용자와 대화중인 상대방들의 list를 출력
    @GetMapping("/{userId}")
    public ResponseEntity<List<ChatUserDTO>> getAllChatList(@PathVariable("userId") Long userId) {
        List<ChatUserDTO> list = new ArrayList<>();
        list = messageRoomService.getAllChatList(userId);
        log.info("접근함");
        return ResponseEntity.ok(list);
    }

//    상대방과의 대화 내역을 출력
    @GetMapping("/{msgRoomId}/detail")
    public ResponseEntity<List<ChatMsgListDTO>> getAllChatMessage(@PathVariable("msgRoomId") Long msgRoomId){
        List<ChatMsgListDTO> list = new ArrayList<>();
        list = messageService.getAllChatMessage(msgRoomId);

        return ResponseEntity.ok(list);
    }

//    보낸 메세지를 저장
    @PostMapping("/messages")
    public void saveChatMessage(@RequestBody ChatMsgListDTO chatMsgListDTO){
        messageService.saveChatMessage(chatMsgListDTO);
    }
}
