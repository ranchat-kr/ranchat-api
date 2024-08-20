package com.ranchat.chatting.matching.scheduler;

import com.ranchat.chatting.common.component.WebSocketSessionRepository;
import com.ranchat.chatting.common.web.WebsocketResponse;
import com.ranchat.chatting.matching.service.MatchService;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.service.CreateRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class MatchScheduler {
    private final MatchService matchService;
    private final CreateRoomService createRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(cron = "0/1 * * * * *")
    public void match() {
        var matchedUsers = matchService.match();

        for (var matchedUserIds : matchedUsers) {
            var roomId = createRoomService.create(
                new CreateRoomService.Requirement(
                    matchedUserIds,
                    Optional.of(
                        "RandomChat-%s".formatted(UUID.randomUUID())
                    ),
                    ChatRoom.RoomType.RANDOM
                )
            );

            for (var userId : matchedUserIds) {
                messagingTemplate.convertAndSendToUser(
                    userId,
                    "/queue/v1/matching/success",
                    WebsocketResponse.success(
                        Map.of("roomId", roomId)
                    )
                );
            }
        }
    }
}
