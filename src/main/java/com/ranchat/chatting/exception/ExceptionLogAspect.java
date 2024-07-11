package com.ranchat.chatting.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ExceptionLogAspect {
    private final ErrorNotificationSender errorNotificationSender;

    @AfterThrowing(pointcut = "within(@org.springframework.web.bind.annotation.RestController *)", throwing = "exception")
    public void writeFailLog(JoinPoint joinPoint, Exception exception) throws RuntimeException {
        var errorMessage = """
            %s.%s Failure - Args: %s
            """
            .formatted(
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs())
            );

        errorNotificationSender.send(errorMessage);
        log.warn(errorMessage);
    }
}
