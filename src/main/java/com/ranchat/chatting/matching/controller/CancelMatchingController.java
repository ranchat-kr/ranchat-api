package com.ranchat.chatting.matching.controller;

import com.ranchat.chatting.matching.service.ApplyMatchingService;
import com.ranchat.chatting.matching.service.CancelMatchingService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/match")
@RequiredArgsConstructor
public class CancelMatchingController {
    private final CancelMatchingService service;

    @MessageMapping("/v1/matching/cancel")
    void applyMatching(Request request) {
        service.cancel(request.userId());
    }

    record Request(
        @NotBlank(message = "userId 는 필수값입니다.")
        String userId
    ) {
    }
}
