package com.ranchat.chatting.exception;

import com.ranchat.chatting.common.web.WebsocketResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.MDC;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import static com.ranchat.chatting.common.support.ExceptionUtils.getMostSpecificCauseMessage;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class WebSocketExceptionHandler {
    private static final String ERROR_QUEUE_DESTINATION = "/queue/errors";
    private final ErrorNotificationSender errorNotificationSender;

    @MessageExceptionHandler({
        ApplicationException.class,
        BadRequestException.class
    })
    @SendToUser(ERROR_QUEUE_DESTINATION)
    WebsocketResponse<Void> handleException(ApplicationException e,
                                            StompHeaderAccessor stompHeaderAccessor) {
        logWithBaseFormat(e, stompHeaderAccessor, false);
        errorNotificationSender.send(e);

        return WebsocketResponse.custom(e.status(), e.getMessage());
    }

    @MessageExceptionHandler(ServerException.class)
    @SendToUser(ERROR_QUEUE_DESTINATION)
    WebsocketResponse<?> handleException(ServerException e,
                                         StompHeaderAccessor stompHeaderAccessor) {
        logWithBaseFormat(e, stompHeaderAccessor, true);
        return WebsocketResponse.custom(e.status(), e.getMessage());
    }

    @MessageExceptionHandler(MaxUploadSizeExceededException.class)
    @SendToUser(ERROR_QUEUE_DESTINATION)
    WebsocketResponse<Void> handleException(MaxUploadSizeExceededException e,
                                            StompHeaderAccessor stompHeaderAccessor) {
        logWithBaseFormat(e, stompHeaderAccessor, false);
        return WebsocketResponse.fail("파일 최대 업로드 가능 용량을 초과했습니다.");
    }

    @MessageExceptionHandler(BindException.class)
    @SendToUser(ERROR_QUEUE_DESTINATION)
    WebsocketResponse<Void> handleBindException(BindException e,
                                                StompHeaderAccessor stompHeaderAccessor) {
        logWithBaseFormat(e, stompHeaderAccessor, false);
        return WebsocketResponse.fail(
            e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage()
        );
    }

    @MessageExceptionHandler({
        IllegalArgumentException.class,
        IllegalStateException.class,
        HttpRequestMethodNotSupportedException.class,
        MethodArgumentTypeMismatchException.class,
        ConstraintViolationException.class
    })
    @SendToUser(ERROR_QUEUE_DESTINATION)
    WebsocketResponse<Void> handleBadRequestException(Exception e,
                                                      StompHeaderAccessor stompHeaderAccessor) {
        logWithBaseFormat(e, stompHeaderAccessor, false);
        return WebsocketResponse.fail(e.getMessage());
    }

    @MessageExceptionHandler
    @SendToUser(ERROR_QUEUE_DESTINATION)
    WebsocketResponse<Void> handleUncaughtException(Exception e,
                                                    StompHeaderAccessor stompHeaderAccessor) {
        logWithBaseFormat(e, stompHeaderAccessor, true);
        return WebsocketResponse.error(e.getMessage());
    }

    private void logWithBaseFormat(Exception e,
                                   StompHeaderAccessor stompHeaderAccessor,
                                   boolean isServerError) {
        logWithBaseFormat(
            e,
            getMostSpecificCauseMessage(e),
            stompHeaderAccessor,
            isServerError
        );
    }

    private void logWithBaseFormat(Exception e,
                                   String message,
                                   StompHeaderAccessor stompHeaderAccessor,
                                   boolean isServerError) {
        String format;
        Object[] objs;

        if (e instanceof ApplicationException ae) {
            format = "{}. status = {}, message = {}";
            objs = new Object[] {
                ae.getClass().getSimpleName(),
                ae.status(),
                message,
                ae
            };
        } else {
            format = "{}. message = {}";
            objs = new Object[] {
                e.getClass().getSimpleName(),
                message,
                e
            };
        }

        logWithContext(
            format,
            objs,
            stompHeaderAccessor,
            isServerError
        );
    }

    private void logWithContext(String format,
                                Object[] args,
                                StompHeaderAccessor stompHeaderAccessor,
                                boolean isServerError) {
        putRequestContext(stompHeaderAccessor);

        if (isServerError) {
            log.error(
                format,
                args
            );
        } else {
            log.warn(
                format,
                args
            );
        }

        clearContext();
    }

    private void putRequestContext(StompHeaderAccessor stompHeaderAccessor) {
        MDC.put(
            "request.destination",
            stompHeaderAccessor.getDestination()
        );

        stompHeaderAccessor.getMessageHeaders()
            .forEach((header, value) ->
                MDC.put(
                    "request.header." + header,
                    value.toString()
                )
            );
    }

    private void clearContext() {
        MDC.clear();
    }
}
