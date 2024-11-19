package com.ranchat.chatting.room.domain;

import com.ranchat.chatting.common.entity.BaseEntity;
import com.ranchat.chatting.common.support.JsonUtils;
import com.ranchat.chatting.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_DATE;

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
        this.title = initTitle(title, type, participants);
        this.type = type;
        this.participants = participants;
    }

    private String initTitle(String title,
                             RoomType type,
                             List<ChatParticipant> participants) {
        if (type == RoomType.GPT) {
            var now = LocalDate.now();
            return "GPT 채팅방 %s".formatted(now.format(ISO_DATE));
        }

        if (type == RoomType.RANDOM) {
            // TODO: 채팅방 이름 로직 구현2
            var userNames = participants.stream()
                .map(ChatParticipant::name)
                .toList();

            return JsonUtils.stringify(userNames);
        }

        return title;
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

    public ChatParticipant getParticipant(long participantId) {
        return participants.stream()
            .filter(participant -> participant.id() == participantId)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("참가자가 아닙니다. participantId: " + participantId));
    }

    public ChatParticipant getParticipant(String userId) {
        return participants.stream()
            .filter(participant -> participant.userId().equals(userId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("참가자가 아닙니다. userId: " + userId));
    }

    public List<ChatParticipant> getOtherParticipants(long participantId) {
        return participants.stream()
            .filter(participant -> participant.id() != participantId)
            .toList();
    }

    public List<ChatParticipant> getOtherParticipants(String userId) {
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
        GPT,
        PUBLIC
    }
}
