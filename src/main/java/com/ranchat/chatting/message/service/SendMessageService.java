package com.ranchat.chatting.message.service;

import com.ranchat.chatting.common.support.Status;
import com.ranchat.chatting.exception.BadRequestException;
import com.ranchat.chatting.message.domain.ChatMessage;
import com.ranchat.chatting.message.listener.SendMessageEventListener;
import com.ranchat.chatting.message.repository.ChatMessageRepository;
import com.ranchat.chatting.message.vo.ChatMessageVo;
import com.ranchat.chatting.room.domain.ChatParticipant;
import com.ranchat.chatting.room.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ranchat.chatting.common.support.Status.ROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SendMessageService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ChatMessageVo send(Requirement requirement) {
        var room = chatRoomRepository.findById(requirement.roomId())
            .orElseThrow(() -> new BadRequestException(ROOM_NOT_FOUND));
        var participant = room.getParticipant(requirement.userId());
        var message = createMessage(requirement, participant);
        var savedMessage = chatMessageRepository.save(message);

        eventPublisher.publishEvent(
            new SendMessageEventListener.Event(savedMessage.id())
        );

        return ChatMessageVo.of(savedMessage, participant);
    }

    private ChatMessage createMessage(Requirement requirement,
                                      ChatParticipant participant) {
        return new ChatMessage(
            requirement.roomId(),
            participant.id(),
            requirement.content(),
            ChatMessage.MessageType.CHAT,
            ChatMessage.ContentType.TEXT,
            ChatMessage.SenderType.USER
        );
    }

    public record Requirement(
        Long roomId,
        String userId,
        String content
    ) {
    }
}
