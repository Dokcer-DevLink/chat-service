package com.example.chattingservice.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatDto implements Serializable {

    public enum MessageType {
        ENTER, TALK, LEAVE, EXITED;

    }
    @NotNull(message = "{request.required}")
    private MessageType type; // 필수값
    @NotBlank(message = "{request.required}")
    private String roomUuid; // 필수값
    @NotBlank(message = "{request.required}")
    private String senderUuid; //필수값
    @NotNull(message = "{request.required}")
    private LocalDateTime messageTime; //필수값 + 바인딩 에러
    private String message;

    public static ChatDto getInstanceExit(String roomUUID, String userUUID ){
        return ChatDto.builder()
                .roomUuid(roomUUID)
                .senderUuid(userUUID)
                .type(MessageType.EXITED)
                .build();
    }

}
