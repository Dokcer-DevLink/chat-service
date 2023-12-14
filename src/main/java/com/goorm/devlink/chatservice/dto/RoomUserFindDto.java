package com.example.chattingservice.dto;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RoomUserFindDto {

    private String userUuid;
    private String targetUuid;

    public static RoomUserFindDto getInstance(String userUuid, String targetUuid){
        return RoomUserFindDto.builder()
                .userUuid(userUuid)
                .targetUuid(targetUuid)
                .build();
    }
}
