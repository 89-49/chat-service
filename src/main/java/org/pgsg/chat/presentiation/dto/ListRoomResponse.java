package org.pgsg.chat.presentiation.dto;

import lombok.Builder;
import org.pgsg.chat.application.dto.RoomInfo;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ListRoomResponse(
        UUID roomId,
        String productName,
        String sellerNickname,
        String buyerNickname,
        String roomStatus,
        LocalDateTime lastMessageAt,
        String lastMessage
) {
    public static ListRoomResponse from(RoomInfo roomInfo) {
        return ListRoomResponse.builder()
                .roomId(roomInfo.roomId())
                .productName(roomInfo.productName())
                .sellerNickname(roomInfo.sellerNickname())
                .buyerNickname(roomInfo.buyerNickname())
                .roomStatus(roomInfo.roomStatus())
                .lastMessageAt(roomInfo.lastMessageAt())
                .lastMessage(roomInfo.lastMessage())
                .build();
    }
}
