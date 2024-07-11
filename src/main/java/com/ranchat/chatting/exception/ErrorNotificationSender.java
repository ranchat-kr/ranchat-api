package com.ranchat.chatting.exception;

public interface ErrorNotificationSender {

    void send(String message, Throwable e);

    void send(Throwable e);

    void send(String message);
}
