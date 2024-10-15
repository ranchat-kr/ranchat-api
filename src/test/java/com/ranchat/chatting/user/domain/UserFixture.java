package com.ranchat.chatting.user.domain;

import com.ranchat.chatting.common.FixtureReflectionUtils;
import com.ranchat.chatting.common.TestFixture;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserFixture implements TestFixture<User> {
    private String id = "testUserId";
    private String name = "testUser";

    @Override
    public User build() {
        var user = new User();
        FixtureReflectionUtils.reflect(user, this);
        return user;
    }
}
