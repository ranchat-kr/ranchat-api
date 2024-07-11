package com.ranchat.chatting.exception;

import com.ranchat.chatting.common.support.Status;

public class UnAuthorizedException extends BadRequestException {
    public UnAuthorizedException() {
        super(Status.UNAUTHORIZED);
    }
}
