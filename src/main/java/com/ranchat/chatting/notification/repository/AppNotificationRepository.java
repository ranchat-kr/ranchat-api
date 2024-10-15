package com.ranchat.chatting.notification.repository;

import com.ranchat.chatting.notification.domain.AppNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppNotificationRepository extends JpaRepository<AppNotification, Long> {

    boolean existsByUserIdAndDeviceName(String userId, String deviceName);

    Optional<AppNotification> findByUserIdAndAgentId(String userId, String agentId);
}
