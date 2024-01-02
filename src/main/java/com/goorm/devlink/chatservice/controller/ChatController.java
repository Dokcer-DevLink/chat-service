package com.goorm.devlink.chatservice.controller;


import com.goorm.devlink.chatservice.config.properties.vo.KafkaConfigVo;
import com.goorm.devlink.chatservice.dto.ChatDto;
import com.goorm.devlink.chatservice.service.ChatRoomService;
import com.goorm.devlink.chatservice.vo.RoomUserState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
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
        log.info("======= Kafka Consumer ========");
        log.info("전송자 : {}", chatDto.getSenderUuid());
        log.info("타입 : {}",chatDto.getType());
        log.info("메시지 : {}", chatDto.getMessage());
        log.info("채팅방 : {}", chatDto.getRoomUuid());
        log.info("======= ============== ========");
    }

    @MessageMapping("/chat/sessions")
    public void setSessionOptions(@Payload ChatDto chatDto, StompHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("userUuid", chatDto.getSenderUuid());
        headerAccessor.getSessionAttributes().put("roomUuid", chatDto.getRoomUuid());
        chatRoomService.updateEnterUserState(RoomUserState.IN,chatDto);
        log.info("======== 세션 정보 설정 ( /pub/chat/sessions ) =========");
        log.info("세션ID : {}",headerAccessor.getSessionId());
        log.info("유저 : {}",chatDto.getSenderUuid());
        log.info("채팅방 : {}",chatDto.getRoomUuid());
        log.info("====================================================");

    }

    @MessageMapping("/chat/leave")
    public void leaveChatRoom(@Payload ChatDto chatDto, @Header("simpSessionId") String sessionId){
        chatRoomService.updateRoomUserState(RoomUserState.LEAVE,chatDto);
        log.info("======== 채팅창 벗어나기 [ LEAVE ] ===========");
        log.info("세션Id: {}",sessionId);
        log.info("유저 : {}",chatDto.getSenderUuid());
        log.info("채팅방 : {}",chatDto.getRoomUuid());
        log.info("==========================================");
    }

    @EventListener
    public void handleWebSocketConnecListener(SessionConnectEvent event) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("======== STOMP 세션 생성 =========");
        log.info("세션ID : {}",stompHeaderAccessor.getSessionId());
        log.info("메시지 : {} ", event.getMessage());
        log.info("===============================-");
    }

    // 세션이 종료된 경우
    // 세션이 자동으로 종료되는 경우 -> StompHeaderAccessor에 데이터가 담기지 않아 NullPointer Exception이 발생한다.
    @EventListener
    public void webSocketDisconnectionListener(SessionDisconnectEvent event){
        log.info("--------Into webSocketDisconnectionListener Method--------");
        log.info("event.getCloseStatus().getCode(): {}", event.getCloseStatus().getCode());
        if(event.getCloseStatus().getCode() != 1000) return;
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        boolean isUnsubscribe = (boolean)headerAccessor.getSessionAttributes().get("unSubscribe");
        String userUuid = (String) headerAccessor.getSessionAttributes().get("userUuid");
        String roomUuid = (String) headerAccessor.getSessionAttributes().get("roomUuid");

        log.info("======== STOMP 세션 종료 =========");
        log.info("세션ID : {}",headerAccessor.getSessionId());
        log.info("유형 : {}", ( isUnsubscribe ) ? "채팅방 나가기 이벤트에 의한 STOMP 세션 종료" : "단순 STOMP 세션 종료 ( 브라우저 종료 or 새로운 세션 생성 )");
        log.info("유저 : {}",userUuid);
        log.info("채팅방 : {}",roomUuid);
        log.info("메시지 : {} ", event.getMessage());
        log.info("===============================");
        if(roomUuid!=null&&userUuid!=null&&isUnsubscribe){
            ChatDto chatDtoExit = chatRoomService.doExitUserProcess(ChatDto.getInstanceExit(roomUuid, userUuid));
            template.convertAndSend("/sub/chat/room/" + chatDtoExit.getRoomUuid(), chatDtoExit);
        }

    }

}
