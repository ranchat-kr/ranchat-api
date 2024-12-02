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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ranchat.chatting.common.support.Status.USER_NOT_FOUND;
import static com.ranchat.chatting.user.domain.User.GPT_ID;

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
            .collect(Collectors.toList());


        if (requirement.roomType() == ChatRoom.RoomType.GPT) {
            participants.add(
                new ChatParticipant(
                GPT_ID,
                "GPT"
                )
            );
        }

        var chatRoom = switch (requirement.roomType()) {
            case RANDOM -> new ChatRoom(ChatRoom.RoomType.RANDOM, participants);
            case GPT -> new ChatRoom(ChatRoom.RoomType.GPT, participants);
            case PUBLIC -> throw new BadRequestException(Status.NOT_IMPLEMENTED);
        };

        return chatRoomRepository.save(chatRoom)
            .id();
    }

    public record Requirement(
        List<String> userIds,
        Optional<String> title,
        ChatRoom.RoomType roomType
    ) {
    }
}
