package com.ranchat.chatting.notification.service;

import com.ranchat.chatting.notification.domain.AppNotificationFixture;
import com.ranchat.chatting.notification.repository.AppNotificationRepository;
import com.ranchat.chatting.user.domain.UserFixture;
import com.ranchat.chatting.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UpdateAppNotificationServiceTest {
    @InjectMocks
    private UpdateAppNotificationService service;
    @Mock
    private AppNotificationRepository appNotificationRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void update() {
        //given
        var appNotification = new AppNotificationFixture()
            .setAllowsNotification(false)
            .build();
        var user = new UserFixture()
            .setId(appNotification.userId())
            .build();
        var command = new UpdateAppNotificationService.Command(
            user.id(),
            appNotification.agentId(),
            true
        );
        given(userRepository.findById(command.userId()))
            .willReturn(Optional.of(user));
        given(appNotificationRepository.findByUserIdAndAgentId(command.userId(), command.agentId()))
            .willReturn(Optional.of(appNotification));

        //when
        var throwable = catchThrowable(() -> service.update(command));

        //then
        assertThat(throwable).isNull();
    }
}