package com.ranchat.chatting.message.repository;

import com.ranchat.chatting.message.domain.ChatMessage;
import com.ranchat.chatting.message.service.GetMessageListService;
import com.ranchat.chatting.message.vo.ChatMessageVo;
import com.ranchat.chatting.room.service.CreateRoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepositoryCustom {

    Page<ChatMessageVo> findChatMessageList(GetMessageListService.Requirement requirement);
}
