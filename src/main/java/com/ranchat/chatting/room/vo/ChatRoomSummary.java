package com.ranchat.chatting.room.vo;

import com.ranchat.chatting.room.domain.ChatRoom;

import java.time.LocalDateTime;

public record ChatRoomSummary(
    long id,
    String title,
    ChatRoom.RoomType type,
    String latestMessage,
    LocalDateTime latestMessageAt
) {
}
