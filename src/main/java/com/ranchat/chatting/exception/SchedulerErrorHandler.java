package com.ranchat.chatting.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

import java.time.LocalDateTime;


@Component
@Slf4j
@RequiredArgsConstructor
public class SchedulerErrorHandler implements ErrorHandler {
    private final ErrorNotificationSender errorNotificationSender;

    @Override
    public void handleError(Throwable e) {
        var message = """
            [Scheduler Exception] %s
            """.formatted(e.getMessage());

        errorNotificationSender.send(message, e);
        log.error("[Scheduler Exception][{}]", LocalDateTime.now(), e);
    }
}
