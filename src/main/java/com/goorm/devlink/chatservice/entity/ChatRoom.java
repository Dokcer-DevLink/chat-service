package com.example.chattingservice.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor( access = AccessLevel.PROTECTED )
@AllArgsConstructor
@Getter
@Setter
@SequenceGenerator(
        name= "ROOM_SEQ_GENERATOR",
        sequenceName = "CHAT_ROOM_SEQ",
        initialValue = 1, allocationSize = 1
)
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "ROOM_SEQ_GENERATOR")
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "room_uuid", unique = true)
    private String roomUuid;

    @Column(name = "recent_message")
    private String recentMessage;

    @Column(name = "recent_date")
    private LocalDateTime recentDate;

    @Column(name = "message_count", nullable = false)
    private int messageCount;

    @OneToMany(mappedBy = "chatRoom")
    private List<RoomUser> roomUsers = new ArrayList<>();

    @OneToMany(mappedBy="chatRoom")
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public static ChatRoom getInstance(String roomUuid){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomUuid(roomUuid);
        return chatRoom;
    }

    public static ChatRoom getInstance( ){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomUuid(UUID.randomUUID().toString());
        return chatRoom;
    }
}
