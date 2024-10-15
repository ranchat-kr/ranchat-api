package com.ranchat.chatting.notification.controller;

import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.notification.domain.AppNotification;
import com.ranchat.chatting.notification.service.CreateAppNotificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/app-notifications")
@RequiredArgsConstructor
public class CreateAppNotificationController {
    private final CreateAppNotificationService service;

    @PostMapping
    ApiResponse<Void> create(@Valid @RequestBody Request request) {
        var command = new CreateAppNotificationService.Command(
            request.userId(),
            request.agentId(),
            request.osType(),
            request.deviceName(),
            request.allowsNotification()
        );

        service.create(command);
        return ApiResponse.success();
    }

    record Request(
        @NotBlank(message = "userId는 필수입니다.")
        String userId,
        @NotBlank(message = "agentId는 필수입니다.")
        String agentId,
        @NotNull(message = "osType은 필수입니다.")
        AppNotification.OperatingSystemType osType,
        @NotBlank(message = "deviceName은 필수입니다.")
        String deviceName,
        @NotNull(message = "allowsNotification는 필수입니다.")
        Boolean allowsNotification
    ) {
    }
}
