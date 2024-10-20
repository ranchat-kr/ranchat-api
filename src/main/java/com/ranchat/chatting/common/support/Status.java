package com.ranchat.chatting.common.support;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum Status {
    // common
    SUCCESS("성공"),
    ERROR("에러"),
    FAIL("요청을 처리할 수 없습니다."),
    NOT_FOUND("리소스를 찾을 수 없습니다."),
    INVALID_PARAMETER("파라미터가 올바르지 않습니다."),
    UNKNOWN("정의 되지 않은 상태입니다."),
    UNAUTHORIZED("인증되지 않은 사용자입니다."),
    FAIL_TO_UPLOAD("파일 업로드에 실패했습니다."),

    // room
    ROOM_NOT_FOUND("채팅방을 찾을 수 없습니다."),

    // user
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),

    // message
    MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다."),
    NOT_ALLOW_TO_SEND_GPT_MESSAGE_BY_ROOM_TYPE("해당 방에는 GPT 메시지를 보낼 수 없습니다."),

    // appNotification
    APP_NOTIFICATION_NOT_FOUND("알림을 찾을 수 없습니다."),
    ALREADY_EXISTS_APP_NOTIFICATION("이미 등록된 알림이 있습니다."),

    // fcm client
    FAIL_TO_SEND_FCM_MESSAGE("FCM 메시지 전송에 실패했습니다.");



    private final String message;

    public static Status from(String text) {
        return Arrays.stream(Status.values())
            .filter(status -> status.name().equals(text))
            .findAny()
            .orElse(UNKNOWN);
    }
}

