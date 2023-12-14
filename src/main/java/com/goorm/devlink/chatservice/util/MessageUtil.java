package com.example.chattingservice.util;


import com.example.chattingservice.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public class MessageUtil {

    private final MessageSource messageSource;

    public String getRoomNoSuchMessage(String roomUuid){
        return messageSource.getMessage("request.nosuch.room",new String[]{roomUuid}, Locale.KOREA);
    }

    public String getUserNoSuchMessage(ChatDto chatDto){
        return messageSource.getMessage("request.nosuch.user", new String[]{chatDto.getRoomUuid(),
                chatDto.getSenderUuid()},Locale.KOREA);
    }

    public String getNoEqualUserUuidMessage(String userUuid,String targetUuid){
        return messageSource.getMessage("request.noequal.user", new String[]{userUuid,targetUuid},Locale.KOREA);
    }

    public String getNoIsoTypeMessage(){
        return messageSource.getMessage("request.noisotype.datae",new String[] {},Locale.KOREA);
    }
}
