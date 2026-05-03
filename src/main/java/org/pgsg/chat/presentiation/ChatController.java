package org.pgsg.chat.presentiation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.chat.application.dto.RoomInfo;
import org.pgsg.chat.application.service.ChatService;
import org.pgsg.chat.application.service.query.ChatQueryService;
import org.pgsg.chat.presentiation.dto.ChatRelay;
import org.pgsg.chat.presentiation.dto.FindRoomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatQueryService chatQueryService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/{roomId}/message")
    public void chatRelay(@DestinationVariable("roomId") UUID roomId, ChatRelay chatRelay) {
        log.info("메세지 전송: roomId: {}, chatRelay: {}", roomId, chatRelay);
        chatService.addMessage(roomId, chatRelay.type(), chatRelay.message());
        messagingTemplate.convertAndSend("/topic/room/" + roomId, chatRelay);
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<FindRoomResponse> findRoom(@PathVariable("roomId") UUID roomId){
        FindRoomResponse response = FindRoomResponse.from(chatQueryService.findChatRoom(roomId));
        return ResponseEntity.ok(response);
    }

}
