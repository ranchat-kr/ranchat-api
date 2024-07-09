package com.ranchat.chatting.message.service;

import com.ranchat.chatting.message.repository.ChatMessageRepository;
import com.ranchat.chatting.message.vo.ChatMessageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetMessageListService {
    private final ChatMessageRepository chatMessageRepository;

    public Page<ChatMessageVo> get(Requirement requirement) {
        return chatMessageRepository.findChatMessageList(requirement);
    }

    public record Requirement(
        long roomId,
        Optional<LocalDateTime> createdAt,
        Pageable pageable
    ) {
    }
}
