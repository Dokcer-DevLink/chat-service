package com.goorm.devlink.chatservice.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Slf4j
@Component
public class StompCommandHandler implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if(StompCommand.UNSUBSCRIBE.equals(headerAccessor.getCommand())){
            headerAccessor.getSessionAttributes().put("unSubscribe",true);
        }

        else if(headerAccessor.getSessionAttributes().get("unSubscribe") == null){
            headerAccessor.getSessionAttributes().put("unSubscribe", false);
        }

        log.info("[CHAT-SERVICE] {} 요청 ", headerAccessor.getCommand());
        log.info("[CHAT-SERVICE] 세션ID : {} ", headerAccessor.getSessionId());
        log.info("[CHAT-SERVICE] 구독취소여부 : {} ",headerAccessor.getSessionAttributes().get("unSubscribe"));

        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {



    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        log.info("[CHAT-SERVICE] {} 요청 종료", headerAccessor.getCommand());
    }


}
