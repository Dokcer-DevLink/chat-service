package com.goorm.devlink.chatservice.controller;


import com.goorm.devlink.chatservice.config.properties.vo.KafkaConfigVo;
import com.goorm.devlink.chatservice.dto.ChatDto;
import com.goorm.devlink.chatservice.service.ChatRoomService;
import com.goorm.devlink.chatservice.util.MessageUtil;
import com.goorm.devlink.chatservice.vo.ChatRoomCreateResponse;
import com.goorm.devlink.chatservice.vo.ChatRoomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
//@CrossOrigin
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final KafkaTemplate<String, ChatDto> kafkaTemplate;
    private final KafkaConfigVo kafkaConfigVo;
    private final MessageUtil messageUtil;

    @PostMapping("/send")
    public void publishMessageToTopic(@RequestBody @Valid ChatDto chatDto)
            throws ExecutionException, InterruptedException {
        if(chatDto.getType()==ChatDto.MessageType.TALK) chatRoomService.processSendMessage(chatDto);
        kafkaTemplate.send(kafkaConfigVo.getTopicName(),chatDto).get();
    }

    // 채팅리스트 화면 조회
    @GetMapping("/api/chat/rooms")
    public ResponseEntity<List<ChatRoomResponse>> findAllChatRoomsByUserId(@RequestHeader("userUuid") @NotBlank String userUuid){
        return ResponseEntity.ok(chatRoomService.findAllChatRoomByUserId(userUuid));
    }

    // 채팅방 생성
    @GetMapping("/api/chat/createroom")
    public ResponseEntity<ChatRoomCreateResponse> createRoom(@RequestHeader("userUuid") @NotBlank String userUuid,
                                                             @RequestParam @NotBlank String targetUuid) {
        if(userUuid.equals(targetUuid)) {
            throw new IllegalArgumentException(messageUtil.getNoEqualUserUuidMessage(userUuid,targetUuid));
        }
        String roomUuid = chatRoomService.findOrCreateChatRoom(userUuid, targetUuid);
        return ResponseEntity.ok(ChatRoomCreateResponse.getInstance(roomUuid));
    }

    @GetMapping("/api/chat")
    public ResponseEntity<Slice<ChatDto>> findAllChatList(@RequestParam("roomUuid") @NotBlank String roomUuid,
                                          @RequestParam("beforeTime") @NotBlank String beforeTime ){
        return ResponseEntity.ok(chatRoomService.findAllChatList(roomUuid,
                LocalDateTime.parse(beforeTime, DateTimeFormatter.ISO_DATE_TIME)));
    }



}
