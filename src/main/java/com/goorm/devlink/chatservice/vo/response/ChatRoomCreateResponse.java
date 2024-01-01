package com.goorm.devlink.chatservice.vo.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatRoomCreateResponse {

    private String roomUuid;

    public static ChatRoomCreateResponse getInstance(String roomUuid){
        return ChatRoomCreateResponse.builder()
                .roomUuid(roomUuid)
                .build();
    }
}
