package com.ranchat.chatting.notification.repository;

import com.ranchat.chatting.notification.domain.AppNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppNotificationHistoryRepository extends JpaRepository<AppNotificationHistory, Long> {
}