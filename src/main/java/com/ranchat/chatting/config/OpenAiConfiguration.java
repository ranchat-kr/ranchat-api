package com.ranchat.chatting.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfiguration {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder
            .defaultSystem("실제 20~30대 한국인이 반말하는 컨셉입니다. 유머러스한 대답 희망")
            .build();
    }
}