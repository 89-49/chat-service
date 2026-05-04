package org.pgsg.chat.application.service.query;

import lombok.RequiredArgsConstructor;
import org.pgsg.chat.application.dto.FindChatRoomQuery;
import org.pgsg.chat.application.dto.RoomInfo;
import org.pgsg.chat.domain.exception.ChatRoomNotFoundException;
import org.pgsg.chat.domain.model.Room;
import org.pgsg.chat.domain.model.RoomId;
import org.pgsg.chat.domain.repository.ChatRoomQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatQueryService {

    private final ChatRoomQueryRepository roomQueryRepository;

    // 단건 조회
    public RoomInfo findChatRoom(UUID roomId){

        Room room = roomQueryRepository.findById(RoomId.of(roomId))
                .orElseThrow(ChatRoomNotFoundException::new);

        return RoomInfo.from(room);
    }

    // 단체 조회
    public Page<RoomInfo> findChatRooms(FindChatRoomQuery query,  Pageable pageable){
        return roomQueryRepository.findAll(FindChatRoomQuery.toSearch(query),pageable).map(RoomInfo::from);
    }

}
