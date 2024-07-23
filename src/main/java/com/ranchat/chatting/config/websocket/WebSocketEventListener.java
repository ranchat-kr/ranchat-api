package com.ranchat.chatting.config.websocket;

import com.ranchat.chatting.common.component.WebSocketSessionRepository;
import com.ranchat.chatting.common.support.Status;
import com.ranchat.chatting.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

@Component
public class WebSocketEventListener {
    private final WebSocketSessionRepository webSocketSessionRepository;

    public WebSocketEventListener(@Qualifier("concurrentMapSessionRepository") WebSocketSessionRepository webSocketSessionRepository) {
        this.webSocketSessionRepository = webSocketSessionRepository;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        var headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        var sessionId = headerAccessor.getSessionId();
        var userId = headerAccessor.getNativeHeader("userId")
            .stream()
            .findFirst()
            .orElseThrow(() -> new BadRequestException(Status.UNAUTHORIZED, "header에 userId가 필요합니다."));

        webSocketSessionRepository.save(userId, sessionId);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        var headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        var sessionId = headerAccessor.getSessionId();

        webSocketSessionRepository.deleteBySessionId(sessionId);
    }
}