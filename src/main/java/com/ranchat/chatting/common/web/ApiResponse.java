package com.ranchat.chatting.common.web;

import com.ranchat.chatting.common.support.Status;

import java.time.LocalDateTime;

public record ApiResponse<T>(
    Status status,
    String message,
    LocalDateTime serverDateTime,
    T data
) {
    public static ApiResponse<Void> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
            Status.SUCCESS,
            Status.SUCCESS.message(),
            LocalDateTime.now(),
            data
        );
    }

    public static <T> ApiResponse<T> fail(String message) {
        return fail(
            message,
            null
        );
    }

    public static <T> ApiResponse<T> fail(String message,
                                          T data) {
        return new ApiResponse<>(
            Status.FAIL,
            message,
            LocalDateTime.now(),
            data
        );
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(
            message,
            null
        );
    }

    public static <T> ApiResponse<T> error(Status status,
                                           String message) {
        return new ApiResponse<>(
            status,
            message,
            LocalDateTime.now(),
            null
        );
    }

    public static <T> ApiResponse<T> error(String message,
                                           T errors) {
        return new ApiResponse<>(
            Status.ERROR,
            message,
            LocalDateTime.now(),
            errors
        );
    }

    public static <T> ApiResponse<T> custom(Status status,
                                            String message,
                                            T data) {
        return new ApiResponse<>(
            status,
            message,
            LocalDateTime.now(),
            data
        );
    }
}

