package com.ranchat.chatting.room.controller;

import com.ranchat.chatting.common.constant.TopicType;
import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.common.web.WebsocketResponse;
import com.ranchat.chatting.room.service.ExitRoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ExitRoomController {
    private final ExitRoomService service;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/v1/rooms/{roomId}/exit")
    void exit(@DestinationVariable Long roomId,
              @Valid @Payload Request request) {
        var message = service.exit(roomId, request.userId());

        messagingTemplate.convertAndSend(
            TopicType.RECEIVE_NEW_MESSAGE.endpoint().formatted(roomId),
            WebsocketResponse.success(message)
        );
    }

    record Request(
        @NotBlank(message = "userId는 필수입니다.")
        String userId
    ) {
    }
}
