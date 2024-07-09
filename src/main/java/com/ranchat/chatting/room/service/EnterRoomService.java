package com.ranchat.chatting.room.service;

import com.ranchat.chatting.message.domain.ChatMessage;
import com.ranchat.chatting.message.repository.ChatMessageRepository;
import com.ranchat.chatting.message.vo.ChatMessageVo;
import com.ranchat.chatting.room.repository.ChatRoomRepository;
import com.ranchat.chatting.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnterRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Transactional
    public Optional<ChatMessageVo> enter(long roomId,
                                         String userId) {
        var room = chatRoomRepository.findById(roomId).orElseThrow();
        var user = userRepository.findById(userId).orElseThrow();

        if (room.isParticipant(user.id())) {
            return Optional.empty();
        }

        room.enter(user);
        chatRoomRepository.save(room);

        var participant = room.getParticipant(user.id());
        var message = new ChatMessage(
            roomId,
            participant.id(),
            user.name() + "님이 입장하셨습니다.",
            ChatMessage.MessageType.ENTER,
            ChatMessage.ContentType.TEXT,
            ChatMessage.SenderType.SYSTEM
        );
        var savedMessage = chatMessageRepository.save(message);

        return Optional.of(
            ChatMessageVo.of(savedMessage, participant)
        );
    }
}
