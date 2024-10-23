package com.ss.heartlinkapi.message.controller;

import com.ss.heartlinkapi.message.dto.ApplyMessageDTO;
import com.ss.heartlinkapi.message.dto.ChatMsgListDTO;
import com.ss.heartlinkapi.message.dto.ChatUserDTO;
import com.ss.heartlinkapi.message.service.MessageRoomService;
import com.ss.heartlinkapi.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    public void saveChatMessage(@RequestParam("file") MultipartFile multipartFile,
                                @RequestParam("msgRoomId") Long msgRoomId,
                                @RequestParam("content") String content,
                                @RequestParam("emoji") String emoji,
                                @RequestParam("senderId") Long senderId,
//                                @RequestParam("lastMessageTime") LocalDateTime lastMessageTime,
                                @RequestParam("isRead") boolean isRead){

        if (multipartFile.isEmpty()) {
            log.info("파일이 비었습니다.");
        }
        else{
            log.info("파일은: {}", multipartFile.getOriginalFilename());
            log.info("파일 사이즈는: {}", multipartFile.getSize());
        }

        try {
//            이미지를 저장하는 과정
            String filePath = "src/main/resources/static/img/" + multipartFile.getOriginalFilename();
            multipartFile.transferTo(new File(filePath));

//            이미지를 가져올 경로를 저장하는 과정
            String ImportPath = "http://localhost:9090/img/" + multipartFile.getOriginalFilename();

            ChatMsgListDTO chatMsgListDTO = new ChatMsgListDTO().builder()
                    .msgRoomId(msgRoomId)
                    .senderId(senderId)
                    .content(content)
                    .emoji(emoji)
                    .imageUrl(ImportPath)
//                    .lastMessageTime(lastMessageTime)
                    .isRead(isRead)
                    .build();

            messageService.saveChatMessage(chatMsgListDTO);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
//    비공개 사용자에게 메시지 요청보내기
    @PostMapping("/message/apply")
    public ResponseEntity<String> applyMessage(@RequestBody ApplyMessageDTO applyMessageDTO) {

        messageRoomService.applyMessage(applyMessageDTO);

        return ResponseEntity.ok("apply success");
    }

//    비공개 사용자 메세지 요청 거절
    @GetMapping("/message/rejection/{msgRoomId}")
    public ResponseEntity<String> applyRejection(@PathVariable("msgRoomId") Long msgRoomId) {

        messageRoomService.applyRejection(msgRoomId);

        return ResponseEntity.ok("rejection success");
    }

//    비공개 사용자 메시지 요청 수락
    @PutMapping("/message/accept/{msgRoomId}")
    public ResponseEntity<String> applyAccept(@PathVariable("msgRoomId") Long msgRoomId){

        messageRoomService.applyAccept(msgRoomId);

        return ResponseEntity.ok("accept success");
    }
}
