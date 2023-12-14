package com.goorm.devlink.chatservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor( access = AccessLevel.PROTECTED )
public class RoomUserCreateDto {

    private String userUuid ;
    private int readMessageCount;
    private RoomUserState userState;

    public static RoomUserCreateDto getInstance(String userUuid){
        return RoomUserCreateDto.builder()
                .userUuid(userUuid)
                .userState(RoomUserState.CREATE)
                .build();
    }

}