package com.ranchat.chatting.room.repository;

import com.ranchat.chatting.room.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
