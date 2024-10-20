package com.ranchat.chatting.room.service;

import com.ranchat.chatting.room.exception.RoomNotFoundException;
import com.ranchat.chatting.room.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivateParticipantService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void activate(Requirement requirement) {
        var room = chatRoomRepository.findById(requirement.roomId())
            .orElseThrow(RoomNotFoundException::new);
        var participant = room.getParticipant(requirement.userId());

        participant.activate();
    }

    public record Requirement(
        long roomId,
        String userId
    ) {
    }
}
