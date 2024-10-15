package com.ss.heartlinkapi.message.controller;

import com.ss.heartlinkapi.message.dto.ChatUserDTO;
import com.ss.heartlinkapi.message.entity.MessageEntity;
import com.ss.heartlinkapi.message.service.MessageRoomService;
import com.ss.heartlinkapi.message.service.MessageService;
import com.ss.heartlinkapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
public class MessageController {

    private final MessageRoomService messageRoomService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<ChatUserDTO>> getAllChatList(@PathVariable("userId") Long userId) {
        List<ChatUserDTO> list = new ArrayList<>();
        list = messageRoomService.getAllChatList(userId);
        

        return ResponseEntity.ok(list);
    }

}
