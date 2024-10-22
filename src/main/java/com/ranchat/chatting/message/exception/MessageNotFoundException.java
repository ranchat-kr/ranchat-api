package com.ranchat.chatting.message.exception;

import com.ranchat.chatting.common.support.Status;
import com.ranchat.chatting.exception.BadRequestException;

public class MessageNotFoundException extends BadRequestException {
    public MessageNotFoundException() {
        super(Status.MESSAGE_NOT_FOUND);
    }
}
