package com.ranchat.chatting.common.web;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final ChatClient chatClient;

    @GetMapping("/test")
    public String test(String message) {
        var a = chatClient.prompt()
            .system("한국에 이루다라는 20대 여성과 랜덤 채팅하는 어플이 있었는데 20대 여성으로 컨셉을 잡고 채팅을 해줘")
            .user(message)
            .call()
            .content();

        return a;
    }
}
