package com.example.chattingservice.repository;

import com.example.chattingservice.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom{

    ChatRoom findChatRoomByRoomUuid(String roomUuid);
}
