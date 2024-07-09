package com.ranchat.chatting.message.domain;

import com.ranchat.chatting.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Table(name = "chat_messages")
@Entity
@Getter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "chat_room_id", nullable = false)
    private long roomId;

    @Column(nullable = false)
    private long participantId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SenderType senderType;

    public ChatMessage(long roomId,
                       long participantId,
                       String content,
                       MessageType messageType,
                       ContentType contentType,
                       SenderType senderType) {
        this.roomId = roomId;
        this.participantId = participantId;
        this.content = content;
        this.messageType = messageType;
        this.contentType = contentType;
        this.senderType = senderType;
    }

    public enum MessageType{
        ENTER,
        CHAT,
        LEAVE,
        NOTICE
    }

    public enum ContentType{
        TEXT,
        FILE
    }

    public enum SenderType{
        USER,
        ADMIN,
        SYSTEM
    }
}
