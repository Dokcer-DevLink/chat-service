package com.goorm.devlink.chatservice.repository.impl;


import com.goorm.devlink.chatservice.dto.ChatDto;
import com.goorm.devlink.chatservice.repository.RoomUserRepositoryCustom;
import com.goorm.devlink.chatservice.vo.RoomUserState;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;


import static com.goorm.devlink.chatservice.entity.QChatRoom.chatRoom;
import static com.goorm.devlink.chatservice.entity.QRoomUser.roomUser;

public class RoomUserRepositoryImpl implements RoomUserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RoomUserRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional
    public long updateRoomUserState(RoomUserState userState, ChatDto chatDto) {
        return queryFactory
                .update(roomUser)
                .set(roomUser.updatedDate, LocalDateTime.now())
                .set(roomUser.userState, userState)
                .where(
                        roomUser.userUuid.eq(chatDto.getSenderUuid()),
                        roomUser.chatRoom.eq(
                                JPAExpressions.selectFrom(chatRoom)
                                        .where(chatRoom.roomUuid.eq(chatDto.getRoomUuid()))
                        )
                ).execute();
    }

    @Override
    @Transactional
    public long updateReadMessageCount(ChatDto chatDto) {
        return queryFactory
                .update(roomUser)
                .set(roomUser.updatedDate, LocalDateTime.now())
                .set(roomUser.readMessageCount,
                        JPAExpressions.select(chatRoom.messageCount)
                                .from(chatRoom)
                                .where(chatRoom.roomUuid.eq(chatDto.getRoomUuid())))
                .where(
                        roomUser.userUuid.eq(chatDto.getSenderUuid()),
                        roomUser.chatRoom.eq(
                                JPAExpressions.selectFrom(chatRoom)
                                        .where(chatRoom.roomUuid.eq(chatDto.getRoomUuid()))
                        )
                ).execute();
    }
}
