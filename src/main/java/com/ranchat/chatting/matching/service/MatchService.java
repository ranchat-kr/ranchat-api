package com.ranchat.chatting.matching.service;

import com.ranchat.chatting.matching.repository.MatchingRepository;
import com.ranchat.chatting.room.domain.ChatParticipant;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.repository.ChatRoomRepository;
import com.ranchat.chatting.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchingRepository matchingRepository;

    @Transactional
    public List<List<String>> match() {
        var waitingUsers = matchingRepository.findWaitingUsers();
        var matchedUsers = new ArrayList<List<String>>();

        if (waitingUsers.size() < 2) {
            return Collections.emptyList();
        }

        Collections.shuffle(waitingUsers);

        for (int i = 2; i <= waitingUsers.size(); i+=2) {
            var matchedUserIds = waitingUsers.subList(i - 2, i);
            matchedUsers.add(matchedUserIds);
        }

        matchingRepository.deleteUsers(
            matchedUsers.stream()
                .flatMap(List::stream)
                .toList()
        );

        return matchedUsers;
    }
}
