package com.goorm.devlink.chatservice.util;



import com.goorm.devlink.chatservice.dto.ChatDto;
import com.goorm.devlink.chatservice.dto.ChatRoomDto;
import com.goorm.devlink.chatservice.dto.RoomUserCreateDto;
import com.goorm.devlink.chatservice.entity.ChatMessage;
import com.goorm.devlink.chatservice.entity.ChatRoom;
import com.goorm.devlink.chatservice.entity.RoomUser;
import com.goorm.devlink.chatservice.vo.ChatRoomResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class ModelMapperUtil {

    private final ModelMapper mapper;
    public RoomUser convertToRoomUser(String userUuid){
        return mapper.map(RoomUserCreateDto.getInstance(userUuid),RoomUser.class);
    }

    public List<ChatRoomResponse> convertToChatRoomResponseList(List<ChatRoom> chatRoomList, String sender){
        return convertChatRoomDtoListToChatRoomResponseList(convertChatRoomListToChatRoomDtoList(chatRoomList),sender);
    }

    public ChatMessage convertToChatMessage(ChatDto chatDto, ChatRoom chatRoom){
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ChatMessage chatMessage = mapper.map(chatDto, ChatMessage.class);
        chatMessage.updateChatRoom(chatRoom);
        return chatMessage;
    }

    private List<ChatRoomDto> convertChatRoomListToChatRoomDtoList(List<ChatRoom> chatRoomList){
        List<ChatRoomDto> chatRoomDtoList = new ArrayList<>();
        chatRoomList.forEach( chatRoom -> {
            ChatRoomDto chatRoomDto = mapper.map(chatRoom, ChatRoomDto.class);
            chatRoomDtoList.add(chatRoomDto);
        });

        return chatRoomDtoList;
    }

    private List<ChatRoomResponse> convertChatRoomDtoListToChatRoomResponseList(List<ChatRoomDto> chatRoomDtoList, String sender){

        List<ChatRoomResponse> chatRoomResponseList = new ArrayList<>();
        chatRoomDtoList.forEach(chatRoomDto -> {
            chatRoomResponseList.add(ChatRoomResponse.convert(chatRoomDto,sender));
        });

        return chatRoomResponseList;
    }

}
