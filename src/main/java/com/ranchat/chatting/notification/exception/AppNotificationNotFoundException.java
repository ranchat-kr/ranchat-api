package com.ranchat.chatting.notification.exception;

import com.ranchat.chatting.common.support.Status;
import com.ranchat.chatting.exception.BadRequestException;

public class AppNotificationNotFoundException extends BadRequestException {
    public AppNotificationNotFoundException() {
        super(Status.APP_NOTIFICATION_NOT_FOUND);
    }
}
