package com.ranchat.chatting.report.service;

import com.ranchat.chatting.report.component.ReportValidator;
import com.ranchat.chatting.report.domain.Report;
import com.ranchat.chatting.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateReportService {
    private final ReportRepository reportRepository;
    private final ReportValidator reportValidator;

    @Transactional
    public void create(Requirement requirement) {
        var report = new Report(
            requirement.roomId(),
            requirement.reporterId(),
            requirement.reportedUserId(),
            requirement.reportType(),
            requirement.reportReason()
        );

        reportValidator.validateCreation(report);
        reportRepository.save(report);
    }

    public record Requirement(
        long roomId,
        String reporterId,
        String reportedUserId,
        Report.ReportType reportType,
        String reportReason
    ) {
    }
}
