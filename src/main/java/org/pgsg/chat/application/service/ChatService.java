package org.pgsg.chat.application.service;

import lombok.RequiredArgsConstructor;
import org.pgsg.chat.application.dto.CreateChatRoomCommand;
import org.pgsg.chat.domain.event.ChatEvents;
import org.pgsg.chat.domain.exception.ChatRoomNotFoundException;
import org.pgsg.chat.domain.model.Room;
import org.pgsg.chat.domain.model.RoomId;
import org.pgsg.chat.domain.model.SenderType;
import org.pgsg.chat.domain.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatEvents chatEvents;

    // 채팅방 생성
    @Transactional
    public void createRoom(CreateChatRoomCommand dto) {
        chatRoomRepository.save(dto.toChatRoom());
    }

    // 거래 성공 처리
    @Transactional
    public void complete(UUID roomId){
        getRoom(roomId).complete(chatEvents);
    }

    // 거래 취소 처리
    @Transactional
    public void cancel(UUID roomId){
        getRoom(roomId).cancel(chatEvents);
    }

    // 채팅 대화 기록
    @Transactional
    public void addMessage(UUID roomId, String senderType, String message) {
        getRoom(roomId).addMessage(SenderType.valueOf(senderType), message);
    }

    private Room getRoom(UUID roomId){
        return chatRoomRepository.findById(RoomId.of(roomId))
                .orElseThrow(ChatRoomNotFoundException::new);
    }

}
