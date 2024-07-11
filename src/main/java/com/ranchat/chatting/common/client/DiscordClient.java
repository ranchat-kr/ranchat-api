package com.ranchat.chatting.common.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DiscordClient {
    private final RestTemplate restTemplate;
    @Value("${application.discord.webhook.url}")
    private String webhookUrl;

    public void sendMessage(String message) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var payload = Map.of("content", message);
        var request = new HttpEntity<>(payload, headers);

        restTemplate.postForLocation(webhookUrl, request);
    }
}
