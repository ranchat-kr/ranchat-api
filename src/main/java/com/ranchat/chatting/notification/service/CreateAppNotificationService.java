package com.ranchat.chatting.notification.service;

import com.ranchat.chatting.notification.domain.AppNotification;
import com.ranchat.chatting.notification.repository.AppNotificationRepository;
import com.ranchat.chatting.user.exception.UserNotFoundException;
import com.ranchat.chatting.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateAppNotificationService {
    private final AppNotificationRepository appNotificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(Command command) {
        var user = userRepository.findById(command.userId())
            .orElseThrow(UserNotFoundException::new);
        var existing = appNotificationRepository.findByUserIdAndAgentId(
            command.userId(),
            command.agentId()
        );

        AppNotification appNotification;

        if (existing.isEmpty()) {
            appNotification = new AppNotification(
                user.id(),
                command.agentId(),
                command.osType(),
                command.deviceName(),
                command.allowsNotification()
            );
        } else {
            appNotification = existing.get();
            appNotification.update(command.allowsNotification());
        }

        appNotificationRepository.save(appNotification);
    }

    public record Command(
        String userId,
        String agentId,
        AppNotification.OperatingSystemType osType,
        String deviceName,
        boolean allowsNotification
    ) {
    }
}
