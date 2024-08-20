package com.ranchat.chatting.room.controller;

import com.ranchat.chatting.common.constant.TopicType;
import com.ranchat.chatting.room.service.EnterRoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
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
