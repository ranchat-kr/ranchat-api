package com.ranchat.chatting.matching.controller;

import com.ranchat.chatting.matching.service.ApplyMatchingService;
import com.ranchat.chatting.message.domain.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/match")
@RequiredArgsConstructor
public class ApplyMatchingController {
    private final ApplyMatchingService service;

    @MessageMapping("/v1/matching/apply")
    void applyMatching() {
        service.applyMatching();
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
