package com.example.chattingservice.repository.impl;

import com.example.chattingservice.dto.ChatDto;
import com.example.chattingservice.dto.RoomUserFindDto;
import com.example.chattingservice.repository.ChatRoomRepositoryCustom;
import com.example.chattingservice.vo.RoomUserState;
import com.example.chattingservice.entity.ChatRoom;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import static com.example.chattingservice.entity.QChatRoom.chatRoom;
import static com.example.chattingservice.entity.QRoomUser.roomUser;

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
    public ChatRoom findChatRoomByUserId(RoomUserFindDto roomUserFindDto) {

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

        return chatRoom1;

    }

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
                .fetch();
    }




}
