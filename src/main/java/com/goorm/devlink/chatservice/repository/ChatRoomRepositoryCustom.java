package com.example.chattingservice.repository;

import com.example.chattingservice.dto.ChatDto;
import com.example.chattingservice.dto.RoomUserFindDto;
import com.example.chattingservice.entity.ChatRoom;

import java.util.List;

public interface ChatRoomRepositoryCustom {



    long updateRecentMessageData(ChatDto chatDto);

    ChatRoom findChatRoomByUserId(RoomUserFindDto roomUserFindDto);

    List<ChatRoom> findAllChatRoomByUserId(String userUuid); //수정


}
