package com.ranchat.chatting.room.repository;

import com.ranchat.chatting.room.vo.ChatRoomSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomRepositoryCustom {

    Page<ChatRoomSummary> findJoinedChatRooms(Pageable pageable, String userId);
}
