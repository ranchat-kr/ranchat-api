package com.ranchat.chatting.room.service;

import com.ranchat.chatting.room.domain.ChatParticipant;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.repository.ChatRoomRepository;
import com.ranchat.chatting.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public void create(Requirement requirement) {
        var user  = userRepository.findById(requirement.userId()).orElseThrow();
        var participant = new ChatParticipant(user.id(), user.name());
        var chattingRoom = new ChatRoom(
            requirement.title(),
            requirement.roomType(),
            List.of(participant)
        );

        chattingRoom.enter(user);
        chatRoomRepository.save(chattingRoom);
    }

    public record Requirement(
        String userId,
        String title,
        ChatRoom.RoomType roomType
    ) {
    }
}
