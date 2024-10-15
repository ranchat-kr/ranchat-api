package com.ranchat.chatting.notification.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

class AppNotificationTest {

    @Nested
    class validate {
        @Test
        void success() {
            //given
            var appNotification = new AppNotificationFixture().build();

            //when
            var throwable = catchThrowable(appNotification::validate);

            //then
            assertThat(throwable).isNull();
        }
    }

    @Nested
    class update {
        @Test
        void success() {
            //given
            var appNotification = new AppNotificationFixture()
                .setAllowsNotification(false)
                .build();
            var allowsNotification = true;

            //when
            appNotification.update(allowsNotification);

            //then
            assertThat(appNotification.allowsNotification()).isEqualTo(allowsNotification);
        }
    }
}