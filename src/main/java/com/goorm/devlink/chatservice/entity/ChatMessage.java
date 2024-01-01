package com.goorm.devlink.chatservice.entity;


import com.goorm.devlink.chatservice.dto.ChatDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor( access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ChatMessage extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Lob
    @Column(name="message")
    private String message;

    @Column(name="message_time")
    private LocalDateTime messageTime;

    @Column(name="sender_uuid")
    private String senderUuid;

    @Column(name="room_uuid")
    private String roomUuid;

    @Column(name="type")
    @Enumerated(EnumType.STRING)
    private ChatDto.MessageType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    public void updateChatRoom(ChatRoom chatRoom){
        this.chatRoom = chatRoom;
    }
    public ChatDto convert(){
        return ChatDto.builder()
                .message(message)
                .messageTime(messageTime)
                .senderUuid(senderUuid)
                .roomUuid(roomUuid)
                .type(type)
                .build();
    }
}
