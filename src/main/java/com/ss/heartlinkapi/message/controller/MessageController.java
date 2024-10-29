package com.ss.heartlinkapi.message.controller;

import com.ss.heartlinkapi.login.dto.CustomUserDetails;
import com.ss.heartlinkapi.message.dto.*;
import com.ss.heartlinkapi.message.service.MessageRoomService;
import com.ss.heartlinkapi.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/dm")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageRoomService messageRoomService;
    private final MessageService messageService;

    //    사용자와 대화중인 상대방들의 list를 출력
    @GetMapping
    public ResponseEntity<HashMap<String, Object>> getAllChatList(@AuthenticationPrincipal CustomUserDetails user) {

        HashMap<String, Object> response = new HashMap<>();
        response.put("username",user.getUsername());
        List<ChatUserDTO> list = messageRoomService.getAllChatList(user.getUserId());
        response.put("chatList", list);

        return ResponseEntity.ok(response);
    }

    //    상대방과의 대화 내역을 출력
    @GetMapping("/{msgRoomId}/detail")
    public ResponseEntity<List<ChatMsgListDTO>> getAllChatMessage(@PathVariable("msgRoomId") Long msgRoomId) {
        List<ChatMsgListDTO> list = messageService.getAllChatMessage(msgRoomId);

        return ResponseEntity.ok(list);
    }

//    텍스트 메세지를 저장
    @PostMapping("/messages/text")
    public ResponseEntity<String> createTextMessage(@RequestBody TextMessageDTO textMessageDTO) {

        ChatMsgListDTO chatMsgListDTO = new ChatMsgListDTO().builder()
                .msgRoomId(textMessageDTO.getMsgRoomId())
                .senderId(textMessageDTO.getSenderId())
                .content(textMessageDTO.getContent())
                .lastMessageTime(LocalDateTime.now())
                .isRead(false)
                .build();

        messageService.saveChatMessage(chatMsgListDTO);

        return ResponseEntity.ok("save message");
    }

    //    이미지 파일 또는 gif를 메시지 저장
    @PostMapping("/messages/img")
    public void saveImageMessage(@RequestParam("file") MultipartFile multipartFile,
                                @RequestParam("msgRoomId") Long msgRoomId,
                                @RequestParam("senderId") Long senderId) {

//        이미지가 비었는지 확인
        if (multipartFile.isEmpty()) {
            log.error("이미지가 없습니다.");

        }

        try {
//            현재 heartlink-api폴더 경로를 가져옴.
            String currentPath = Paths.get("").toAbsolutePath().toString();

//            img파일 위치 경로에 파일 이름을 더해 filePath에 저장
            String filePath = currentPath + "/src/main/resources/static/img/" + multipartFile.getOriginalFilename();
            multipartFile.transferTo(new File(filePath));

//            이미지를 가져올 경로를 저장하는 과정
            String ImportPath = "http://localhost:9090/img/" + multipartFile.getOriginalFilename();

            ChatMsgListDTO chatMsgListDTO = new ChatMsgListDTO().builder()
                    .msgRoomId(msgRoomId)
                    .senderId(senderId)
                    .emoji(null)
                    .imageUrl(ImportPath)
                    .lastMessageTime(LocalDateTime.now())
                    .isRead(false)
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
    public ResponseEntity<String> applyAccept(@PathVariable("msgRoomId") Long msgRoomId) {

        messageRoomService.applyAccept(msgRoomId);

        return ResponseEntity.ok("accept success");
    }

    //    사용자가 타 사용자를 차단한 경우 사용자는 타 사용자에게 DM을 보낼 수 없다
    @GetMapping("/message/block")
    public ResponseEntity<String> blockMessage(@RequestBody BlockUserCheckDTO blockUserCheckDTO) {

        boolean result = false;
        result =  messageService.blockMessage(blockUserCheckDTO);

        if(result)
            return ResponseEntity.ok("block user");
        else
            return ResponseEntity.ok("nonblock user");
    }
}
