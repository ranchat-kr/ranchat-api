package com.ranchat.chatting.notification.service;

import com.ranchat.chatting.notification.exception.AppNotificationNotFoundException;
import com.ranchat.chatting.notification.repository.AppNotificationRepository;
import com.ranchat.chatting.user.exception.UserNotFoundException;
import com.ranchat.chatting.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateAppNotificationService {
    private final AppNotificationRepository appNotificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void update(Command command) {
        var user = userRepository.findById(command.userId())
            .orElseThrow(UserNotFoundException::new);
        var appNotification = appNotificationRepository.findByUserIdAndAgentId(
                user.id(),
                command.agentId()
            )
            .orElseThrow(AppNotificationNotFoundException::new);

        appNotification.update(command.allowsNotification());
    }

    public record Command(
        String userId,
        String agentId,
        boolean allowsNotification
    ) {
    }
}
