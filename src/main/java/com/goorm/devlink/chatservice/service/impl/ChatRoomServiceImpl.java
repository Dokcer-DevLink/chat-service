package com.goorm.devlink.chatservice.service.impl;



import com.goorm.devlink.chatservice.config.properties.vo.PageConfigVo;
import com.goorm.devlink.chatservice.dto.ChatDto;
import com.goorm.devlink.chatservice.dto.RoomUserFindDto;
import com.goorm.devlink.chatservice.entity.ChatMessage;
import com.goorm.devlink.chatservice.entity.ChatRoom;
import com.goorm.devlink.chatservice.entity.RoomUser;
import com.goorm.devlink.chatservice.feign.ProfileServiceClient;
import com.goorm.devlink.chatservice.repository.ChatMessageRepository;
import com.goorm.devlink.chatservice.repository.ChatRoomRepository;
import com.goorm.devlink.chatservice.repository.RoomUserRepository;
import com.goorm.devlink.chatservice.service.ChatRoomService;
import com.goorm.devlink.chatservice.util.MessageUtil;
import com.goorm.devlink.chatservice.util.ModelMapperUtil;
import com.goorm.devlink.chatservice.vo.response.ChatRoomResponse;
import com.goorm.devlink.chatservice.vo.RoomUserState;
import com.goorm.devlink.chatservice.vo.response.ProfileSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ModelMapperUtil modelMapperUtil;
    private final ChatRoomRepository chatRoomRepository;
    private final RoomUserRepository roomUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final PageConfigVo pageConfigVo;
    private final MessageUtil messageUtil;
    private final ProfileServiceClient profileServiceClient;


    @Override
    public String findOrCreateChatRoom(String userUuid, String targetUuid) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByUserId(RoomUserFindDto.getInstance(userUuid, targetUuid))
                .orElseGet(()->createChatRoom(userUuid, targetUuid));
        return chatRoom.getRoomUuid();
    }

    @Override
    public void processSendMessage(ChatDto chatDto) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByRoomUuid(chatDto.getRoomUuid())
                .orElseThrow(() -> new NoSuchElementException(messageUtil.getRoomNoSuchMessage(chatDto.getRoomUuid())));

        chatRoomRepository.updateRecentMessageData(chatDto);
        chatMessageRepository.save(modelMapperUtil.convertToChatMessage(chatDto,chatRoom));
    }

    @Override
    public void updateRoomUserState(RoomUserState roomUserState, ChatDto chatDto){

        long count = roomUserRepository.updateRoomUserState(roomUserState, chatDto);
        if ( count <= 0 ) throw new NoSuchElementException(messageUtil.getUserNoSuchMessage(chatDto));
    }

    /** 채팅 리스트 조회하기 **/
    @Override
    public List<ChatRoomResponse> findAllChatRoomByUserId(String userUuid) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllChatRoomByUserId(userUuid);
        return getChatRoomList(chatRooms,userUuid);
    }

    @Override
    public void updateEnterUserState(RoomUserState roomUserState, ChatDto chatDto) {
        long count = roomUserRepository.updateRoomUserState(roomUserState, chatDto);
        if ( count <= 0 ) throw new NoSuchElementException(messageUtil.getUserNoSuchMessage(chatDto));
        roomUserRepository.updateReadMessageCount(chatDto);
    }

    @Override
    public ChatDto doExitUserProcess(ChatDto chatDto) {

        long count = roomUserRepository.updateRoomUserState(RoomUserState.EXITED, chatDto);// User 상태 변경하기
        if ( count <= 0 ) throw new NoSuchElementException(messageUtil.getUserNoSuchMessage(chatDto));

        return chatDto;
    }

    @Override
    public Slice<ChatDto> findAllChatList(String roomUuid, LocalDateTime beforeTime) {
        PageRequest pageRequest = PageRequest.of(pageConfigVo.getOffset(),pageConfigVo.getSize());
        Slice<ChatMessage> chatSliceMessages =
                chatMessageRepository.findChatMessageListByRoomUuid(roomUuid, pageRequest, beforeTime);
        return chatSliceMessages.map(chatMessage -> chatMessage.convert());
    }


    private ChatRoom createChatRoom(String userUuid, String targetUuid) {
        ChatRoom chatRoom = ChatRoom.getInstance();
        RoomUser user = modelMapperUtil.convertToRoomUser(userUuid);
        RoomUser target = modelMapperUtil.convertToRoomUser(targetUuid);

        user.updateChatRoom(chatRoom);
        target.updateChatRoom(chatRoom);
        chatRoomRepository.save(chatRoom);
        roomUserRepository.save(user);
        roomUserRepository.save(target);

        return chatRoom;
    }

    private List<ChatRoomResponse> getChatRoomList(List<ChatRoom> chatRooms,String userUuid){
        return ( chatRooms.size() != 0) ?
                modelMapperUtil.convertToChatRoomResponseList(chatRooms,userUuid,getProfileInfo(chatRooms,userUuid))
                : new ArrayList<>();
    }
    private List<ProfileSimpleResponse> getProfileInfo(List<ChatRoom> chatRooms, String userUuid){
        List<String> targetUserUuids = getTargetUserUuids(chatRooms,userUuid);
        return profileServiceClient.getProfileSimpleInfo(targetUserUuids).getBody();
    }

    private List<String> getTargetUserUuids(List<ChatRoom> chatRoomList,String userUuid){
        List<String> targetUserUuids = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomList) {
            for (RoomUser roomUser : chatRoom.getRoomUsers()) {
                if(!roomUser.getUserUuid().equals(userUuid)) targetUserUuids.add(roomUser.getUserUuid());
            }
        }
        return targetUserUuids;
    }



}
