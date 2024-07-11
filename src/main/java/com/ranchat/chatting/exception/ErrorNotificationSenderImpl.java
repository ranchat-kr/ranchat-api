package com.ranchat.chatting.exception;

import com.ranchat.chatting.common.client.DiscordClient;
import com.ranchat.chatting.common.support.StackTraceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ErrorNotificationSenderImpl implements ErrorNotificationSender {
    private final DiscordClient discordClient;
    private final Environment environment;

    @Override
    public void send(String message,
                     Throwable e) {
        var errorMessage = """
            message: %s
            stacktrace: %s
            """
            .formatted(
                message,
                StackTraceUtils.toString(e)
            );

        send(errorMessage);
    }

    @Override
    public void send(Throwable e) {
        var errorMessage = """
            message: %s
            stacktrace: %s
            """
            .formatted(
                e.getMessage(),
                StackTraceUtils.toString(e)
            );

        send(errorMessage);
    }

    @Override
    public void send(String message) {
        var sendableProfiles = Set.of("dev", "prod");

        if (sendableProfiles.contains(currentProfile())) {
            discordClient.sendMessage(message);
        }
    }

    private String currentProfile() {
        return environment.getActiveProfiles()[0];
    }
}
