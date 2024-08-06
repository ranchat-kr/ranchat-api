package com.ranchat.chatting.room.service;

import com.ranchat.chatting.room.domain.ChatParticipant;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetRoomDetailsService {
    private final ChatRoomRepository roomRepository;

    @Transactional(readOnly = true)
    public RoomDetails get(long roomId, Optional<String> userId) {
        var room = roomRepository.findById(roomId).orElseThrow();

        return RoomDetails.of(room, userId);
    }

    public record RoomDetails(
        long id,
        String title,
        ChatRoom.RoomType type,
        List<Participant> participants
    ) {
        public static RoomDetails of(ChatRoom room, Optional<String> userId) {
            var title = userId.map(room::otherParticipants)
                .map(participants -> participants.stream()
                    .map(ChatParticipant::name)
                    .collect(Collectors.joining(","))
                )
                .orElse(room.title());

            return new RoomDetails(
                room.id(),
                title,
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
