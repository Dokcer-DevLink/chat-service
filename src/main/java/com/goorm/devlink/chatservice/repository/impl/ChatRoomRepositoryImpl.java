package com.goorm.devlink.chatservice.repository.impl;


import com.goorm.devlink.chatservice.dto.ChatDto;
import com.goorm.devlink.chatservice.dto.RoomUserFindDto;
import com.goorm.devlink.chatservice.entity.ChatRoom;
import com.goorm.devlink.chatservice.repository.ChatRoomRepositoryCustom;
import com.goorm.devlink.chatservice.vo.RoomUserState;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.goorm.devlink.chatservice.entity.QChatRoom.chatRoom;
import static com.goorm.devlink.chatservice.entity.QRoomUser.roomUser;


//where 조건절 메소드로 만들어 분리하기
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ChatRoomRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional
    public long updateRecentMessageData(ChatDto chatDto) {

        // ChatRoom Entity 메시지 데이터 수정하기
        queryFactory
                .update(chatRoom)
                .set(chatRoom.updatedDate, LocalDateTime.now())
                .set(chatRoom.recentMessage,chatDto.getMessage())
                .set(chatRoom.recentDate,chatDto.getMessageTime())
                .set(chatRoom.messageCount,chatRoom.messageCount.add(1))
                .where(
                        chatRoom.roomUuid.eq(chatDto.getRoomUuid())
                ).execute();

        // RoomUser Entity 메시지 데이터 수정하기
        queryFactory
                .update(roomUser)
                .set(roomUser.updatedDate, LocalDateTime.now())
                .set(roomUser.readMessageCount, roomUser.readMessageCount.add(1))
                .where(
                        roomUser.userState.eq(RoomUserState.IN),
                        roomUser.chatRoom.eq(
                                JPAExpressions.selectFrom(chatRoom)
                                        .where(chatRoom.roomUuid.eq(chatDto.getRoomUuid()))
                        )
                ).execute();



        return 1; // 임시로 return
    }

    @Override
    public Optional<ChatRoom> findChatRoomByUserId(RoomUserFindDto roomUserFindDto) {

        ChatRoom chatRoom1=  queryFactory
                .select(roomUser.chatRoom)
                .from(roomUser)
                .leftJoin(roomUser.chatRoom, chatRoom)
                .where(
                        roomUser.userUuid.eq(roomUserFindDto.getUserUuid()),
                        roomUser.chatRoom.roomUuid.in(
                                JPAExpressions
                                        .select(roomUser.chatRoom.roomUuid)
                                        .from(roomUser)
                                        .leftJoin(roomUser.chatRoom, chatRoom)
                                        .where(
                                                roomUser.userUuid.eq(roomUserFindDto.getTargetUuid())
                                        )
                        )
                ).fetchOne();

        return Optional.ofNullable(chatRoom1);

    }

    /** 채팅리스트 조회 **/
    @Override
    public List<ChatRoom> findAllChatRoomByUserId(String userUuid) {

        return queryFactory.selectFrom(chatRoom)
                .distinct()
                .leftJoin(chatRoom.roomUsers, roomUser)
                .fetchJoin()
                .where(chatRoom.roomId.in(JPAExpressions.select(roomUser.chatRoom.roomId)
                        .from(roomUser)
                        .where(
                                roomUser.userState.ne(RoomUserState.EXITED),
                                roomUser.userUuid.eq(userUuid)
                        )))
                .orderBy(chatRoom.recentDate.desc())
                .fetch();
    }




}
