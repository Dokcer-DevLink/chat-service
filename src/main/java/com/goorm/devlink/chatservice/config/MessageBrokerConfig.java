package com.example.chattingservice.config;

import com.example.chattingservice.config.properties.vo.StompConfigVo;
import com.example.chattingservice.interceptor.StompCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class MessageBrokerConfig implements WebSocketMessageBrokerConfigurer {

    private final StompConfigVo stompConfigVo;
    private final StompCommandHandler stompCommandHandler;

    // /ws-stomp 엔드포인트에서 SocketJS 연결
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(stompConfigVo.getEndpoint())
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompCommandHandler);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 메시지 구독 요청 URL
        registry.enableSimpleBroker(stompConfigVo.getSub());
        // 메시지 발행 요청 URL
        registry.setApplicationDestinationPrefixes(stompConfigVo.getPub());
    }





}
