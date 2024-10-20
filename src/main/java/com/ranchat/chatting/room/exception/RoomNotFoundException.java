package com.ranchat.chatting.room.exception;

import com.ranchat.chatting.common.support.Status;
import com.ranchat.chatting.exception.BadRequestException;

public class RoomNotFoundException extends BadRequestException {
    public RoomNotFoundException() {
        super(Status.ROOM_NOT_FOUND);
    }
}
