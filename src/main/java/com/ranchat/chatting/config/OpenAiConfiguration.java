package com.ranchat.chatting.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfiguration {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder
            .build();
    }
}