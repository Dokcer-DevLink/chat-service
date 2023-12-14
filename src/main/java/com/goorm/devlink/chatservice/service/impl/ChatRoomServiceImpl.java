package com.goorm.devlink.chatservice.service.impl;



import com.goorm.devlink.chatservice.config.properties.vo.PageConfigVo;
import com.goorm.devlink.chatservice.dto.ChatDto;
import com.goorm.devlink.chatservice.dto.RoomUserFindDto;
import com.goorm.devlink.chatservice.entity.ChatMessage;
import com.goorm.devlink.chatservice.entity.ChatRoom;
import com.goorm.devlink.chatservice.entity.RoomUser;
import com.goorm.devlink.chatservice.repository.ChatMessageRepository;
import com.goorm.devlink.chatservice.repository.ChatRoomRepository;
import com.goorm.devlink.chatservice.repository.RoomUserRepository;
import com.goorm.devlink.chatservice.service.ChatRoomService;
import com.goorm.devlink.chatservice.util.MessageUtil;
import com.goorm.devlink.chatservice.util.ModelMapperUtil;
import com.goorm.devlink.chatservice.vo.ChatRoomResponse;
import com.goorm.devlink.chatservice.vo.RoomUserState;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ModelMapperUtil modelMapperUtil;
    private final ChatRoomRepository chatRoomRepository;
    private final RoomUserRepository roomUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final PageConfigVo pageConfigVo;
    private final MessageUtil messageUtil;


    @Override
    public String findOrCreateChatRoom(String userUuid, String targetUuid) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByUserId(RoomUserFindDto.getInstance(userUuid, targetUuid));
        chatRoom = Optional.ofNullable(chatRoom).orElseGet(()->createChatRoom(userUuid, targetUuid));
        return chatRoom.getRoomUuid();
    }

    @Override
    public void processSendMessage(ChatDto chatDto) {
        ChatRoom chatRoom = Optional.of(chatRoomRepository.findChatRoomByRoomUuid(chatDto.getRoomUuid()))
                .orElseThrow(() -> new NoSuchElementException(messageUtil.getRoomNoSuchMessage(chatDto.getRoomUuid())));

        chatRoomRepository.updateRecentMessageData(chatDto);
        chatMessageRepository.save(modelMapperUtil.convertToChatMessage(chatDto,chatRoom));
    }

    @Override
    public void updateRoomUserState(RoomUserState roomUserState, ChatDto chatDto){

        long count = roomUserRepository.updateRoomUserState(roomUserState, chatDto);
        if ( count <= 0 ) throw new NoSuchElementException(messageUtil.getUserNoSuchMessage(chatDto));
    }

    @Override
    public List<ChatRoomResponse> findAllChatRoomByUserId(String userId) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllChatRoomByUserId(userId);
        return modelMapperUtil.convertToChatRoomResponseList(chatRoomList,userId);
    }

    @Override
    public void updateEnterUserState(RoomUserState roomUserState, ChatDto chatDto) {
        long count = roomUserRepository.updateRoomUserState(roomUserState, chatDto);
        if ( count <= 0 ) throw new NoSuchElementException(messageUtil.getUserNoSuchMessage(chatDto));
        roomUserRepository.updateReadMessageCount(chatDto);
    }

    @Override
    public ChatDto doExitUserProcess(StompHeaderAccessor stompHeaderAccessor) {
        String roomUUID = (String) stompHeaderAccessor.getSessionAttributes().get("roomUUID");
        String userUUID = (String) stompHeaderAccessor.getSessionAttributes().get("userUUID");
        ChatDto chatDtoExit = ChatDto.getInstanceExit(roomUUID, userUUID);
        long count = roomUserRepository.updateRoomUserState(RoomUserState.EXITED, chatDtoExit);// User 상태 변경하기
        if ( count <= 0 ) throw new NoSuchElementException(messageUtil.getUserNoSuchMessage(chatDtoExit));

        return chatDtoExit;
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



}
