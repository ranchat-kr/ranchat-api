package com.ranchat.chatting.user.service;

import com.ranchat.chatting.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserService {
    private final UserRepository userRepository;

    @Transactional
    public void update(Requirement requirement) {
        var user = userRepository.findById(requirement.id()).orElseThrow();

        user.update(requirement.name());
        userRepository.save(user);
    }

    public record Requirement(
        String id,
        String name
    ) {
    }
}

