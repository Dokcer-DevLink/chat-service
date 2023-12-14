package com.goorm.devlink.chatservice.service;


import com.goorm.devlink.chatservice.dto.ChatDto;
import com.goorm.devlink.chatservice.vo.ChatRoomResponse;
import com.goorm.devlink.chatservice.vo.RoomUserState;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRoomService {

    public void processSendMessage(ChatDto chatDto);
    public void updateRoomUserState(RoomUserState roomUserState, ChatDto chatDto);

    public String findOrCreateChatRoom(String userUuid, String targetUuid);

    public List<ChatRoomResponse> findAllChatRoomByUserId(String userId);

    public void updateEnterUserState(RoomUserState roomUserState, ChatDto chatDto);

    public ChatDto doExitUserProcess(StompHeaderAccessor stompHeaderAccessor);

    public Slice<ChatDto> findAllChatList(String roomUuid, LocalDateTime beforeTime);



}
