package com.ranchat.chatting.user.exception;

import com.ranchat.chatting.common.support.Status;
import com.ranchat.chatting.exception.BadRequestException;

public class UserNotFoundException extends BadRequestException {
    public UserNotFoundException() {
        super(Status.USER_NOT_FOUND);
    }
}
