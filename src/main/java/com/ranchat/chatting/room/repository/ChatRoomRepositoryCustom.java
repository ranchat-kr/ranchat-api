package com.ranchat.chatting.room.repository;

import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.service.GetRoomListService;
import com.ranchat.chatting.room.vo.ChatRoomSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepositoryCustom {

    Page<ChatRoomSummary> findAll(GetRoomListService.Request request);
}
