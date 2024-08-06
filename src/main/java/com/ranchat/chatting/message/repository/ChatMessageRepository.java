package com.ranchat.chatting.message.repository;

import com.ranchat.chatting.message.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom {

    @Query("""
    SELECT m
    FROM ChatMessage m
    WHERE m.roomId = :roomId
    AND m.participantId = :participantId
    AND m.messageType = 'ENTER'
    """)
    Optional<ChatMessage> findEnterMessage(long roomId, long participantId);
}
