package com.goorm.devlink.chatservice.repository;

import com.goorm.devlink.chatservice.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom{

    ChatRoom findChatRoomByRoomUuid(String roomUuid);
}
