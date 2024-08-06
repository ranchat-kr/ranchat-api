package com.ranchat.chatting.room.domain;

import com.ranchat.chatting.common.entity.BaseEntity;
import com.ranchat.chatting.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "chat_rooms")
@Entity
@Getter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, name = "room_type")
    @Enumerated(EnumType.STRING)
    private RoomType type;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private List<ChatParticipant> participants = new ArrayList<>();

    public ChatRoom(String title,
                    RoomType type,
                    List<ChatParticipant> participants) {
        this.title = title;
        this.type = type;
        this.participants = participants;
    }

    public void enter(User user) {
        if (isParticipant(user.id())) {
            return;
        }

        participants.add(
            new ChatParticipant(user.id(), user.name())
        );
    }

    public void exit(User user) {
        var participant = getParticipant(user.id());

        participants.remove(participant);
    }

    public ChatParticipant getParticipant(String userId) {
        return participants.stream()
            .filter(participant -> participant.userId().equals(userId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("참가자가 아닙니다. userId: " + userId));
    }

    public List<ChatParticipant> otherParticipants(String userId) {
        return participants.stream()
            .filter(participant -> !participant.userId().equals(userId))
            .toList();
    }

    public boolean isParticipant(String userId) {
        return participants.stream()
            .map(ChatParticipant::userId)
            .anyMatch(userId::equals);
    }

    public enum RoomType {
        RANDOM,
        PUBLIC
    }
}
