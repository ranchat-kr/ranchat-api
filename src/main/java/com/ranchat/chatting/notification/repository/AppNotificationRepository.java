package com.ranchat.chatting.notification.repository;

import com.ranchat.chatting.notification.domain.AppNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AppNotificationRepository extends JpaRepository<AppNotification, Long> {

    boolean existsByUserIdAndDeviceName(String userId, String deviceName);

    Optional<AppNotification> findByUserIdAndAgentId(String userId, String agentId);

    @Query("""
        SELECT n
        FROM AppNotification n
        WHERE n.userId = :userId
        AND n.allowsNotification = true
    """)
    List<AppNotification> findAllActiveNotifications(String userId);
}
