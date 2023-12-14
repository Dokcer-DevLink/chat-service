package com.goorm.devlink.chatservice.repository;


import com.goorm.devlink.chatservice.dto.ChatDto;
import com.goorm.devlink.chatservice.vo.RoomUserState;

public interface RoomUserRepositoryCustom {
    long updateRoomUserState(RoomUserState roomUserState, ChatDto chatDtoExit);

    long updateReadMessageCount(ChatDto chatDto);
}
