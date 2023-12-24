package com.goorm.devlink.chatservice.repository;

import com.goorm.devlink.chatservice.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom{

    Optional<ChatRoom> findChatRoomByRoomUuid(String roomUuid);
}
