package com.ranchat.chatting.common.web;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final ChatClient chatClient;

    @PostMapping("/test")
    String test(@RequestBody Request request) {
        return chatClient.prompt()
            .user(request.message())
            .call()
            .content();
    }

    record Request(
        String message
    ) {
    }
}
