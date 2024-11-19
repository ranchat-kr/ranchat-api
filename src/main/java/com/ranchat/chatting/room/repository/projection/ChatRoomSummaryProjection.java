package com.ranchat.chatting.room.repository.projection;

import com.querydsl.core.annotations.QueryProjection;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.vo.ChatRoomSummary;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomSummaryProjection {
    private long id;
    private String title;
    private ChatRoom.RoomType type;
    private String latestMessage;
    private LocalDateTime latestMessageAt;

    @QueryProjection
    public ChatRoomSummaryProjection(long id, String title, ChatRoom.RoomType type, String latestMessage, LocalDateTime latestMessageAt) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.latestMessage = latestMessage;
        this.latestMessageAt = latestMessageAt;
    }

    public static ChatRoomSummary convert(ChatRoomSummaryProjection projection) {
        return new ChatRoomSummary(
            projection.id(),
            projection.title(),
            projection.type(),
            projection.latestMessage(),
            projection.latestMessageAt()
        );
    }
}
