package com.ranchat.chatting.room.domain;

import com.ranchat.chatting.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Table(name = "chat_participants")
@Entity
@Getter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatParticipant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime lastSeenAt;

    public ChatParticipant(String userId,
                           String name) {
        this.userId = userId;
        this.name = name;
        this.lastSeenAt = LocalDateTime.now();
    }

    public void activate() {
        this.lastSeenAt = LocalDateTime.now();
    }
}
