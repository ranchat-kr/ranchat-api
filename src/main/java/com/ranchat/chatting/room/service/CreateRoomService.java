package com.ranchat.chatting.room.service;

import com.ranchat.chatting.common.support.Status;
import com.ranchat.chatting.exception.BadRequestException;
import com.ranchat.chatting.room.domain.ChatParticipant;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.repository.ChatRoomRepository;
import com.ranchat.chatting.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ranchat.chatting.common.support.Status.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CreateRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public long create(Requirement requirement) {
        var users  = userRepository.findAllById(requirement.userIds());

        if (users.isEmpty()) {
            throw new BadRequestException(USER_NOT_FOUND);
        }

        var participants = users.stream()
            .map(user -> new ChatParticipant(user.id(), user.name()))
            .toList();
        var chattingRoom = new ChatRoom(
            requirement.title(),
            requirement.roomType(),
            participants
        );

        return chatRoomRepository.save(chattingRoom)
            .id();
    }

    public record Requirement(
        List<String> userIds,
        String title,
        ChatRoom.RoomType roomType
    ) {
    }
}
