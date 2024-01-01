package com.goorm.devlink.chatservice.entity;

import com.goorm.devlink.chatservice.vo.RoomUserState;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@AllArgsConstructor
@Builder
public class RoomUser extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_user_id")
    private Long roomUserId;
    @Column( name = "user_uuid", nullable = false )
    private String userUuid;
    @Column( name="read_message_count", nullable = false )
    private int readMessageCount;
    @Enumerated(EnumType.STRING)
    @Column ( name = "user_state" )
    private RoomUserState userState;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private ChatRoom chatRoom;

    public void updateChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }


}
