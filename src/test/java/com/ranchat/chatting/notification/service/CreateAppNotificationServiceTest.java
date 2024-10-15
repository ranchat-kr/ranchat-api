package com.ranchat.chatting.notification.service;

import com.ranchat.chatting.common.support.Status;
import com.ranchat.chatting.notification.domain.AppNotification;
import com.ranchat.chatting.notification.domain.AppNotification.OperatingSystemType;
import com.ranchat.chatting.notification.domain.AppNotificationFixture;
import com.ranchat.chatting.notification.repository.AppNotificationRepository;
import com.ranchat.chatting.user.domain.UserFixture;
import com.ranchat.chatting.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateAppNotificationServiceTest {
    @InjectMocks
    private CreateAppNotificationService service;
    @Mock
    private AppNotificationRepository appNotificationRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void success() {
        // given
        var command = new CreateAppNotificationService.Command(
            "userId",
            "agentId",
            OperatingSystemType.IOS,
            "deviceName",
            true
        );
        var user = new UserFixture()
            .setId(command.userId())
            .build();
        given(userRepository.findById(command.userId()))
            .willReturn(Optional.of(user));
        given(appNotificationRepository.findByUserIdAndAgentId(command.userId(), command.agentId()))
            .willReturn(Optional.empty());

        // when
        var throwable = catchThrowable(() -> service.create(command));

        // then
        assertThat(throwable).isNull();
        var argumentCaptor = ArgumentCaptor.forClass(AppNotification.class);
        verify(appNotificationRepository).save(argumentCaptor.capture());
        var capturedValue = argumentCaptor.getValue();
        assertThat(capturedValue.userId()).isEqualTo(command.userId());
        assertThat(capturedValue.osType()).isEqualTo(command.osType());
        assertThat(capturedValue.deviceName()).isEqualTo(command.deviceName());
        assertThat(capturedValue.allowsNotification()).isEqualTo(command.allowsNotification());
    }

    @Test
    void alreadyExists() {
        // given
        var appNotification = new AppNotificationFixture().build();
        var command = new CreateAppNotificationService.Command(
            appNotification.userId(),
            appNotification.agentId(),
            appNotification.osType(),
            appNotification.deviceName(),
            !appNotification.allowsNotification()
        );
        var user = new UserFixture()
            .setId(command.userId())
            .build();
        given(userRepository.findById(command.userId()))
            .willReturn(Optional.of(user));
        given(appNotificationRepository.findByUserIdAndAgentId(command.userId(), command.agentId()))
            .willReturn(Optional.of(appNotification));

        // when
        var throwable = catchThrowable(() -> service.create(command));

        // then
        assertThat(throwable).isNull();
        var argumentCaptor = ArgumentCaptor.forClass(AppNotification.class);
        verify(appNotificationRepository).save(argumentCaptor.capture());
        var capturedValue = argumentCaptor.getValue();
        assertThat(capturedValue.userId()).isEqualTo(command.userId());
        assertThat(capturedValue.osType()).isEqualTo(command.osType());
        assertThat(capturedValue.deviceName()).isEqualTo(command.deviceName());
        assertThat(capturedValue.allowsNotification()).isEqualTo(command.allowsNotification());
    }
}