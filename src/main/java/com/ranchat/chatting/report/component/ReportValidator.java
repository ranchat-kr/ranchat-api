package com.ranchat.chatting.report.component;

import com.ranchat.chatting.report.domain.Report;

public interface ReportValidator {
    void validateCreation(Report report);
}
