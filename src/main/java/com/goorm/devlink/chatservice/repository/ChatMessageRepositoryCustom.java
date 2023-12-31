package com.goorm.devlink.chatservice.repository;

import com.goorm.devlink.chatservice.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

public interface ChatMessageRepositoryCustom {

    public Slice<ChatMessage> findChatMessageListByRoomUuid(String roomUuid, Pageable pageable, LocalDateTime beforeTime);
}
