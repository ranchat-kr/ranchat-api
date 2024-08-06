package com.ranchat.chatting.room.service;

import com.ranchat.chatting.common.support.Status;
import com.ranchat.chatting.exception.BadRequestException;
import com.ranchat.chatting.message.domain.ChatMessage;
import com.ranchat.chatting.message.repository.ChatMessageRepository;
import com.ranchat.chatting.message.vo.ChatMessageVo;
import com.ranchat.chatting.room.repository.ChatRoomRepository;
import com.ranchat.chatting.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ranchat.chatting.common.support.Status.ROOM_NOT_FOUND;
import static com.ranchat.chatting.common.support.Status.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EnterRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Transactional
    public Optional<ChatMessageVo> enter(long roomId,
                                         String userId) {
        var room = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new BadRequestException(ROOM_NOT_FOUND));
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
        var participant = room.getParticipant(user.id());

        var existMessage = chatMessageRepository.findEnterMessage(roomId, participant.id());

        if (existMessage.isPresent()) {
            return Optional.empty();
        }

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
