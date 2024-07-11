package com.ranchat.chatting.exception;

import com.ranchat.chatting.common.support.Status;

public class ServerException extends ApplicationException {
    public ServerException(Status status) {
        super(status);
    }

    public ServerException(Status status, String message) {
        super(status, message);
    }

    public ServerException(Status status, Throwable cause) {
        super(status, cause);
    }

    public ServerException(Status status, String message, Throwable cause) {
        super(status, message, cause);
    }
}
