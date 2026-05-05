package org.pgsg.chat.application.dto;

import org.pgsg.chat.domain.event.ChatEvents;
import org.pgsg.chat.domain.model.Room;

import java.util.UUID;

public record CreateChatRoomCommand(
        UUID tradeId,
        UUID productId,
        String productName,
        UUID sellerId,
        String sellerNickname,
        UUID buyerId,
        String buyerNickname
) {
    public Room toChatRoom(ChatEvents chatEvents) {
        return Room.builder()
                .tradeId(tradeId)
                .productId(productId)
                .productName(productName)
                .sellerId(sellerId)
                .sellerNickname(sellerNickname)
                .buyerId(buyerId)
                .buyerNickname(buyerNickname)
                .chatEvents(chatEvents)
                .build();
    }
}
