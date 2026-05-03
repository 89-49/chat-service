package org.pgsg.chat.presentiation.dto;

import lombok.Builder;
import org.pgsg.chat.application.dto.RoomInfo;

import java.util.UUID;

@Builder
public record FindRoomResponse(
        UUID roomId,
        UUID sellerId,
        String sellerNickname,
        UUID buyerId,
        String buyerNickname,
        UUID productId,
        String productName,
        String roomStatus
) {
    public static FindRoomResponse from(RoomInfo roomInfo) {
        return FindRoomResponse.builder()
                .roomId(roomInfo.roomId())
                .sellerId(roomInfo.sellerId())
                .sellerNickname(roomInfo.sellerNickname())
                .buyerId(roomInfo.buyerId())
                .buyerNickname(roomInfo.buyerNickname())
                .productId(roomInfo.productId())
                .productName(roomInfo.productName())
                .roomStatus(roomInfo.roomStatus())
                .build();
    }
}
