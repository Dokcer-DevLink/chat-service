package com.goorm.devlink.chatservice.repository;

import com.goorm.devlink.chatservice.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom {

    Slice<ChatMessage> findChatMessagesByRoomUuid(String roomUuid, Pageable pageable);
}
