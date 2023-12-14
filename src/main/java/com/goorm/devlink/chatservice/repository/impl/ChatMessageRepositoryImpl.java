package com.goorm.devlink.chatservice.repository.impl;


import com.goorm.devlink.chatservice.entity.ChatMessage;
import com.goorm.devlink.chatservice.repository.ChatMessageRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.goorm.devlink.chatservice.entity.QChatMessage.chatMessage;


@Slf4j
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {


    private JPAQueryFactory queryFactory;

    public ChatMessageRepositoryImpl(EntityManager entityManager){
        queryFactory = new JPAQueryFactory(entityManager);
    }
    @Override
    public Slice<ChatMessage> findChatMessageListByRoomUuid(String roomUuid, Pageable pageable, LocalDateTime beforeTime) {
        List<ChatMessage> chatMessages = queryFactory
                .selectFrom(chatMessage)
                .where(
                        chatMessage.roomUuid.eq(roomUuid),
                        chatMessage.messageTime.lt(beforeTime)
                        ).limit(pageable.getPageSize() + 1)
                .orderBy(chatMessage.messageTime.desc())
                .fetch();


        return getSliceInstance(pageable,chatMessages);
    }

    private Slice<ChatMessage> getSliceInstance(Pageable pageable,List<ChatMessage> chatMessages){

        boolean hasNext = false;
        if(chatMessages.size() > pageable.getPageSize()){
            chatMessages.remove(pageable.getPageSize());
            hasNext = true;
        }
        Collections.reverse(chatMessages);
        return new SliceImpl<>(chatMessages, pageable,hasNext);
    }

}
