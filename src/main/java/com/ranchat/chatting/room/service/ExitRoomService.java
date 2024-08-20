package com.ranchat.chatting.room.service;

import com.ranchat.chatting.exception.BadRequestException;
import com.ranchat.chatting.message.domain.ChatMessage;
import com.ranchat.chatting.message.repository.ChatMessageRepository;
import com.ranchat.chatting.message.vo.ChatMessageVo;
import com.ranchat.chatting.room.repository.ChatRoomRepository;
import com.ranchat.chatting.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ranchat.chatting.common.support.Status.ROOM_NOT_FOUND;
import static com.ranchat.chatting.common.support.Status.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ExitRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatMessageVo exit(long roomId, String userId) {
        var chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new BadRequestException(ROOM_NOT_FOUND));
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
        var participant = chatRoom.getParticipant(user.id());

        chatRoom.exit(user);
        chatRoomRepository.save(chatRoom);

        var message = new ChatMessage(
            roomId,
            participant.id(),
            participant.name() + "님이 퇴장하셨습니다.",
            ChatMessage.MessageType.LEAVE,
            ChatMessage.ContentType.TEXT,
            ChatMessage.SenderType.SYSTEM
        );
        var savedMessage = chatMessageRepository.save(message);

        return ChatMessageVo.of(savedMessage, participant);
    }
}
