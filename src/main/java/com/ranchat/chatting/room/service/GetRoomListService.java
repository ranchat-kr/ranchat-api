package com.ranchat.chatting.room.service;

import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetRoomListService {
    private final ChatRoomRepository roomRepository;

    public List<RoomSummary> get() {
        var rooms = roomRepository.findAll();

        return rooms.stream()
            .map(room -> new RoomSummary(
                room.id(),
                room.title(),
                room.type()
            ))
            .collect(Collectors.toList());
    }

    public record RoomSummary(
        long id,
        String title,
        ChatRoom.RoomType type
    ) {
    }
}
