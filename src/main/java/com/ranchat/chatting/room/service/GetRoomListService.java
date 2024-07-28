package com.ranchat.chatting.room.service;

import com.ranchat.chatting.room.repository.ChatRoomRepository;
import com.ranchat.chatting.room.vo.ChatRoomSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetRoomListService {
    private final ChatRoomRepository roomRepository;

    public Page<ChatRoomSummary> get(Request request) {
        return roomRepository.findAll(request);
    }

    public record Request(
        Pageable pageable,
        Optional<String> userId
    ) {
    }
}
