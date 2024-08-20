package com.ranchat.chatting.user.vo;

import com.ranchat.chatting.user.domain.User;

public record UserVo(
    String id,
    String name
) {
    public static UserVo from(User user) {
        return new UserVo(
            user.id(),
            user.name()
        );
    }
}
