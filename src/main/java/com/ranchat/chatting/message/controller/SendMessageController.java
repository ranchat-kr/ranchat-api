package com.ranchat.chatting.message.controller;

import com.ranchat.chatting.common.web.WebsocketResponse;
import com.ranchat.chatting.message.domain.ChatMessage;
import com.ranchat.chatting.message.service.SendMessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SendMessageController {
    private final SendMessageService service;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/room/{roomId}/message/send")
    void send(@Valid @Payload Request request,
              @DestinationVariable Long roomId) {
        var requirement = createRequirement(roomId, request);
        var message = service.send(requirement);

        simpMessagingTemplate.convertAndSend(
            "/topic/v1/room/%d/messages/new".formatted(roomId),
            WebsocketResponse.success(message)
        );
    }

    private SendMessageService.Requirement createRequirement(Long roomId,
                                                             Request request) {
        return new SendMessageService.Requirement(
            roomId,
            request.userId(),
            request.content()
        );
    }

    record Request(
        @NotBlank(message = "userId 는 필수값입니다.")
        String userId,
        @NotBlank(message = "content 는 필수값입니다.")
        String content,
        @NotNull(message = "contentType 은 필수값입니다.")
        ChatMessage.ContentType contentType
    ) {
    }
}
