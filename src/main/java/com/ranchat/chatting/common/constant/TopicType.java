package com.ranchat.chatting.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum TopicType {
    RECEIVE_NEW_MESSAGE("/topic/v1/room/%d/messages/new");

    private final String endpoint;
}
