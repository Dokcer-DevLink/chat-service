package com.example.chattingservice.repository;

import com.example.chattingservice.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom {

    Slice<ChatMessage> findChatMessagesByRoomUuid(String roomUuid, Pageable pageable);
}
