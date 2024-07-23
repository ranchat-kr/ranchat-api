package com.ranchat.chatting.matching.service;

import com.ranchat.chatting.matching.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelMatchingService {
    private final MatchingRepository matchingRepository;

    public void cancel(String userId) {
        matchingRepository.deleteUser(userId);
    }
}
