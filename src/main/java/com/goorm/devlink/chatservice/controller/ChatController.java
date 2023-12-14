package com.goorm.devlink.chatservice.controller;


import com.goorm.devlink.chatservice.config.properties.vo.KafkaConfigVo;
import com.goorm.devlink.chatservice.dto.ChatDto;
import com.goorm.devlink.chatservice.service.ChatRoomService;
import com.goorm.devlink.chatservice.vo.RoomUserState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final SimpMessageSendingOperations template;
    private final ChatRoomService chatRoomService;

    @KafkaListener(
            topics = "#{'${data.kafka.topicName}'}",
            groupId = KafkaConfigVo.GROUP_ID
    )
    public void listen(ChatDto chatDto){
        template.convertAndSend("/sub/chat/room/" + chatDto.getRoomUuid(), chatDto);
    }

    @MessageMapping("/chat/sessions")
    public void setSessionOptions(@Payload ChatDto chatDto, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("userUUID", chatDto.getSenderUuid());
        headerAccessor.getSessionAttributes().put("roomUUID", chatDto.getRoomUuid());
        chatRoomService.updateEnterUserState(RoomUserState.IN,chatDto);
    }

    @MessageMapping("/chat/leave")
    public void leaveChatRoom(@Payload ChatDto chatDto){
        chatRoomService.updateRoomUserState(RoomUserState.LEAVE,chatDto);
    }

    // 세션이 종료된 경우
    // 세션이 자동으로 종료되는 경우 -> StompHeaderAccessor에 데이터가 담기지 않아 NullPointer Exception이 발생한다.
    @EventListener
    public void webSocketDisconnectionListener(SessionDisconnectEvent event){
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        boolean isUnsubscribe = (boolean)stompHeaderAccessor.getSessionAttributes().get("isUnsubscribe");
        if(isUnsubscribe){
            ChatDto chatDtoExit = chatRoomService.doExitUserProcess(stompHeaderAccessor);
            template.convertAndSend("/sub/chat/room/" + chatDtoExit.getRoomUuid(), chatDtoExit);
        }

    }

}
