package com.ranchat.chatting.notification.domain;

import com.ranchat.chatting.common.entity.BaseEntityForOnlyCreateAt;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Table(name = "app_notification_histories")
@Entity
@Getter
@ToString
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppNotificationHistory extends BaseEntityForOnlyCreateAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String agentId;

    @Column(nullable = false)
    private long appNotificationId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private String failReason;

    public AppNotificationHistory(Type type,
                                  Status status,
                                  String userId,
                                  String agentId,
                                  long appNotificationId,
                                  String title,
                                  String content,
                                  String failReason) {
        this.type = type;
        this.status = status;
        this.userId = userId;
        this.agentId = agentId;
        this.appNotificationId = appNotificationId;
        this.title = title;
        this.content = content;
        this.failReason = failReason;
    }

    public enum Type {
        SEND_MESSAGE
    }

    public enum Status {
        SUCCESS,
        FAILURE
    }
}