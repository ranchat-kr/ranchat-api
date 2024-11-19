package com.ranchat.chatting.room.service;

import com.ranchat.chatting.room.repository.ChatRoomRepository;
import com.ranchat.chatting.room.vo.ChatRoomSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetJoinedRoomListService {
    private final ChatRoomRepository roomRepository;

    public Page<ChatRoomSummary> get(Request request) {
        return  roomRepository.findJoinedChatRooms(
            request.pageable(),
            request.userId()
        );
    }

    public record Request(
        Pageable pageable,
        String userId
    ) {
    }
}
