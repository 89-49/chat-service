package org.pgsg.chat.presentiation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgsg.chat.application.dto.FindChatRoomQuery;
import org.pgsg.chat.application.service.ChatService;
import org.pgsg.chat.application.service.query.ChatQueryService;
import org.pgsg.chat.presentiation.dto.ChatRelay;
import org.pgsg.chat.presentiation.dto.FindRoomResponse;
import org.pgsg.chat.presentiation.dto.ListRoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/rooms")
    public ResponseEntity<Page<ListRoomResponse>> findRooms(
            @RequestParam(required = false)List<UUID> userIds,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String keyword,
            Pageable pageable
            ){
        Page<ListRoomResponse> response = chatQueryService.findChatRooms(FindChatRoomQuery.of(userIds, productName,userName,keyword),pageable).map(ListRoomResponse::from);

        return ResponseEntity.ok(response);
    }

}
