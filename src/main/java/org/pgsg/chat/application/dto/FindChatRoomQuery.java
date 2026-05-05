package org.pgsg.chat.application.dto;

import lombok.Builder;
import org.pgsg.chat.domain.model.RoomId;
import org.pgsg.chat.domain.repository.ChatRoomSearch;

import java.util.List;
import java.util.UUID;

@Builder
public record FindChatRoomQuery(
        List<RoomId> roomIds,
        List<UUID> userIds,
        String productName, // 상품명
        String userName, //사용자명
        String keyword
) {

    public static ChatRoomSearch toSearch(FindChatRoomQuery query) {
        return ChatRoomSearch.builder()
                .roomIds(query.roomIds())
                .userIds(query.userIds())
                .productName(query.productName())
                .userName(query.userName())
                .keyword(query.keyword())
                .build();
    }

    public static FindChatRoomQuery of(List<UUID> userIds, String productName, String userName, String keyword) {
        return FindChatRoomQuery.builder()
                .userIds(userIds)
                .productName(productName)
                .userName(userName)
                .keyword(keyword)
                .build();
    }

}
