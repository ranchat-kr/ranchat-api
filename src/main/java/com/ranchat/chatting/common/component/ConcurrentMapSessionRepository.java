package com.ranchat.chatting.common.component;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component("concurrentMapSessionRepository")
public class ConcurrentMapSessionRepository implements WebSocketSessionRepository{
    private static final ConcurrentMap<String, String> sessionMap = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, String> userMap = new ConcurrentHashMap<>();

    @Override
    public void save(String userId, String sessionId) {
        sessionMap.put(userId, sessionId);
        userMap.put(sessionId, userId);
    }

    @Override
    public Optional<String> findSessionId(String userId) {
        return Optional.ofNullable(sessionMap.get(userId));
    }

    @Override
    public void deleteByUserId(String userId) {
        var sessionId = sessionMap.get(userId);

        if (sessionId == null) return;

        sessionMap.remove(userId);
        userMap.get(sessionId);
    }

    @Override
    public void deleteBySessionId(String sessionId) {
        var userId = userMap.get(sessionId);

        if (userId == null) return;

        userMap.remove(sessionId);
        sessionMap.get(userId);
    }
}
