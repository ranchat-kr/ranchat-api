package com.ranchat.chatting.room.service;

import com.ranchat.chatting.room.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckExistRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public boolean check(String userId) {
        return chatRoomRepository.existsByUserId(userId);
    }
}
