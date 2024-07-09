package com.ranchat.chatting.message.vo;

import com.ranchat.chatting.message.domain.ChatMessage;
import com.ranchat.chatting.room.domain.ChatParticipant;

import java.time.LocalDateTime;

public record ChatMessageVo(
    long id,
    long roomId,
    String userId,
    long participantId,
    String participantName,
    String content,
    ChatMessage.MessageType messageType,
    ChatMessage.ContentType contentType,
    ChatMessage.SenderType senderType,
    LocalDateTime createdAt
) {
    public static ChatMessageVo of(ChatMessage message,
                                   ChatParticipant participant) {
        return new ChatMessageVo(
            message.id(),
            message.roomId(),
            participant.userId(),
            participant.id(),
            participant.name(),
            message.content(),
            message.messageType(),
            message.contentType(),
            message.senderType(),
            message.createdAt()
        );
    }
}
