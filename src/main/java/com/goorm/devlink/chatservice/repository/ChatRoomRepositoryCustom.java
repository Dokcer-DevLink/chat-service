package com.goorm.devlink.chatservice.repository;


import com.goorm.devlink.chatservice.dto.ChatDto;
import com.goorm.devlink.chatservice.dto.RoomUserFindDto;
import com.goorm.devlink.chatservice.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepositoryCustom {



    long updateRecentMessageData(ChatDto chatDto);

    Optional<ChatRoom> findChatRoomByUserId(RoomUserFindDto roomUserFindDto);

    List<ChatRoom> findAllChatRoomByUserId(String userUuid); //수정


}
