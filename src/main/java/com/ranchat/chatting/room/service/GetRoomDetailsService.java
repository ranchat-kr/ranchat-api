package com.ranchat.chatting.room.service;

import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetRoomDetailsService {
    private final ChatRoomRepository roomRepository;

    @Transactional(readOnly = true)
    public RoomDetails get(long roomId) {
        var room = roomRepository.findById(roomId).orElseThrow();

        return RoomDetails.from(room);
    }

    public record RoomDetails(
        long id,
        String title,
        ChatRoom.RoomType type,
        List<Participant> participants
    ) {
        public static RoomDetails from(ChatRoom room) {
            return new RoomDetails(
                room.id(),
                room.title(),
                room.type(),
                room.participants().stream()
                    .map(participant -> new Participant(
                        participant.id(),
                        participant.userId(),
                        participant.name()
                    ))
                    .toList()
            );
        }

        public record Participant(
            long id,
            String userId,
            String name
        ) {
        }
    }
}
