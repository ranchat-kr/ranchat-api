package com.ranchat.chatting.common.component;

import java.util.Optional;

public interface WebSocketSessionRepository {

    void save(String userId, String sessionId);

    Optional<String> findSessionId(String userId);

    void deleteByUserId(String userId);

    void deleteBySessionId(String sessionId);
}
