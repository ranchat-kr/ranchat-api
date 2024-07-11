package com.ranchat.chatting.common.web;

import com.ranchat.chatting.common.support.Status;

import java.time.LocalDateTime;

public class WebsocketResponse<T> {
    private Status status;
    private String message;
    private LocalDateTime serverDatetime;
    private T data;

    protected WebsocketResponse() {
    }

    private WebsocketResponse(Status status,
                              String message,
                              T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.serverDatetime = LocalDateTime.now();
    }

    public static WebsocketResponse<Void> success() {
        return success(null);
    }

    public static <T> WebsocketResponse<T> success(T data) {
        return new WebsocketResponse<T>(
            Status.SUCCESS,
            Status.SUCCESS.message(),
            data
        );
    }

    public static <T> WebsocketResponse<T> fail(String message) {
        return fail(
            message,
            null
        );
    }

    public static <T> WebsocketResponse<T> fail(String message,
                                                T data) {
        return new WebsocketResponse<T>(
            Status.FAIL,
            message,
            data
        );
    }

    public static <T> WebsocketResponse<T> error(String message) {
        return error(
            message,
            null
        );
    }

    public static <T> WebsocketResponse<T> error(String message,
                                                 T errors) {
        return new WebsocketResponse<T>(
            Status.ERROR,
            message,
            errors
        );
    }

    public static <T> WebsocketResponse<T> custom(Status status,
                                                  T data) {
        return custom(
            status,
            status.message(),
            data
        );
    }

    public static <T> WebsocketResponse<T> custom(Status status,
                                                  String message) {
        return custom(
            status,
            message,
            null
        );
    }

    public static <T> WebsocketResponse<T> custom(Status status,
                                                  String message,
                                                  T data) {
        return new WebsocketResponse<>(
            status,
            message,
            data
        );
    }

    public Status status() {
        return status;
    }

    public String message() {
        return message;
    }

    public LocalDateTime serverDatetime() {
        return serverDatetime;
    }

    public T data() {
        return data;
    }
}
