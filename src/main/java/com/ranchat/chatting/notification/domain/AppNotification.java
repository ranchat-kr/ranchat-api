package com.ranchat.chatting.notification.domain;

import com.ranchat.chatting.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Table(name = "app_notifications")
@Entity
@Getter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppNotification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String agentId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OperatingSystemType osType;

    @Column(nullable = false)
    private String deviceName;

    @Column
    private boolean allowsNotification;

    public AppNotification(String userId,
                           String agentId,
                           OperatingSystemType osType,
                           String deviceName,
                           boolean allowsNotification) {
        this.userId = userId;
        this.agentId = agentId;
        this.osType = osType;
        this.deviceName = deviceName;
        this.allowsNotification = allowsNotification;
        validate();
    }

    public void update(boolean allowsNotification) {
        if (allowsNotification != this.allowsNotification) {
            this.allowsNotification = allowsNotification;
        }

        validate();
    }

    void validate() {
        if (userId == null || userId.isBlank()) {
            throw new InvalidAppNotificationException(
                "userId는 비거나 공백일 수 없습니다. currentValue: %s".formatted(userId)
            );
        }

        if (osType == null) {
            throw new InvalidAppNotificationException(
                "osType은 필수입니다."
            );
        }

        if (deviceName == null || deviceName.isBlank()) {
            throw new InvalidAppNotificationException(
                "deviceName은 비거나 공백일 수 없습니다. currentValue: %s".formatted(deviceName)
            );
        }
    }

    public enum OperatingSystemType {
        ANDROID,
        IOS
    }

    public static class InvalidAppNotificationException extends IllegalArgumentException {
        public InvalidAppNotificationException(String message) {
            super(message);
        }
    }
}
