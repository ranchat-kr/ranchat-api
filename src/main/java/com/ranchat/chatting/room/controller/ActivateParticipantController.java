package com.ranchat.chatting.room.controller;

import com.ranchat.chatting.room.service.ActivateParticipantService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ActivateParticipantController {
    private final ActivateParticipantService service;

    @MessageMapping("/v1/rooms/{roomId}/activate-participant")
    void activate(@DestinationVariable Long roomId,
                  @Valid @Payload EnterRoomController.Request request) {
        var requirement = new ActivateParticipantService.Requirement(
            roomId,
            request.userId()
        );

        service.activate(requirement);
    }

    record Request(
        @NotBlank(message = "userId는 필수입니다.")
        String userId
    ) {
    }
}
