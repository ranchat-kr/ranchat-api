package com.ranchat.chatting.report.domain;

import com.ranchat.chatting.common.entity.BaseEntityForOnlyCreateAt;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Table(name = "reports")
@Entity
@Getter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntityForOnlyCreateAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long roomId;

    @Column(nullable = false)
    private String reporterId;

    @Column(nullable = false)
    private String reportedUserId;

    @Column(name = "report_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportType type;

    @Column(name = "report_reason", nullable = false)
    private String reason;

    public Report(long roomId,
                  String reporterId,
                  String reportedUserId,
                  ReportType type,
                  String reason) {
        this.roomId = roomId;
        this.reporterId = reporterId;
        this.reportedUserId = reportedUserId;
        this.type = type;
        this.reason = reason;
    }

    private void validateCreation() {
        if (roomId <= 0) {
            throw new IllegalArgumentException("roomId는 0보다 커야 합니다.");
        }

        if (reporterId == null || reporterId.isBlank()) {
            throw new IllegalArgumentException("reporterId는 필수입니다.");
        }

        if (reportedUserId == null || reportedUserId.isBlank()) {
            throw new IllegalArgumentException("reportedUserId는 필수입니다.");
        }

        if (type == null) {
            throw new IllegalArgumentException("type은 필수입니다.");
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("reason은 필수입니다.");
        }
    }

    public enum ReportType {
        SPAM,
        HARASSMENT,
        ADVERTISEMENT,
        MISINFORMATION,
        COPYRIGHT_INFRINGEMENT,
        ETC
    }
}
