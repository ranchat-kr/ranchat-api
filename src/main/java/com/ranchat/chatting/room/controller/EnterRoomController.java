package com.ranchat.chatting.room.controller;

import com.ranchat.chatting.common.constant.TopicType;
import com.ranchat.chatting.room.service.EnterRoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EnterRoomController {
    private final EnterRoomService service;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/v1/rooms/{roomId}/enter")
    void enter(@DestinationVariable Long roomId,
               @Valid @Payload Request request) {
        var message = service.enter(roomId, request.userId());

        message.ifPresent(m ->
            messagingTemplate.convertAndSend(
                TopicType.RECEIVE_NEW_MESSAGE.endpoint().formatted(roomId),
                m
            )
        );
    }

    record Request(
        @NotBlank(message = "userId는 필수입니다.")
        String userId
    ) {
    }
}
