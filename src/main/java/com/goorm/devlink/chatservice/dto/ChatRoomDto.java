package com.example.chattingservice.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor( access = AccessLevel.PROTECTED )
public class ChatRoomDto {

    private String roomUuid;
    private String recentMessage;
    private LocalDateTime recentDate;
    private int messageCount;
    private List<RoomUserCreateDto> roomUsers;

    public static ChatRoomDto getInstance(){
        return ChatRoomDto.builder()
                .roomUuid(UUID.randomUUID().toString())
                .build();
    }

}
