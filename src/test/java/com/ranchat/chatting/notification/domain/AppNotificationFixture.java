package com.ranchat.chatting.notification.domain;

import com.ranchat.chatting.common.FixtureReflectionUtils;
import com.ranchat.chatting.common.TestFixture;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AppNotificationFixture implements TestFixture<AppNotification> {
    private Long id = 1L;
    private String userId = "testUserId";
    private String agentId = "testAgentId";
    private AppNotification.OperatingSystemType osType = AppNotification.OperatingSystemType.ANDROID;
    private String deviceName = "testDevice";
    private boolean allowsNotification = true;

    @Override
    public AppNotification build() {
        var appNotification = new AppNotification();
        FixtureReflectionUtils.reflect(appNotification, this);
        return appNotification;
    }
}