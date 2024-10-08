package com.ranchat.chatting.matching.service;

import com.ranchat.chatting.matching.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplyMatchingService {
    private final MatchingRepository matchingRepository;

    public void applyMatching(String userId) {
        matchingRepository.addUser(userId);
    }
}
