package com.ranchat.chatting.report.controller;

import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.report.domain.Report;
import com.ranchat.chatting.report.service.CreateReportService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reports")
@RequiredArgsConstructor
public class CreateReportController {
    private final CreateReportService service;

    @PostMapping
    ApiResponse<Void> create(@Valid @RequestBody Request request) {
        service.create(request.toRequirement());

        return ApiResponse.success();
    }

    record Request(
        @NotNull(message = "roomId는 필수입니다.")
        Long roomId,
        @NotBlank(message = "reporterId는 필수입니다.")
        String reporterId,
        @NotBlank(message = "reportedUserId는 필수입니다.")
        String reportedUserId,
        @NotNull(message = "reportType은 필수입니다.")
        Report.ReportType reportType,
        @NotBlank(message = "reportReason은 필수입니다.")
        String reportReason
    ) {

        /*
         to json
         */
        public CreateReportService.Requirement toRequirement() {
            return new CreateReportService.Requirement(
                roomId,
                reporterId,
                reportedUserId,
                reportType,
                reportReason
            );
        }
    }
}
