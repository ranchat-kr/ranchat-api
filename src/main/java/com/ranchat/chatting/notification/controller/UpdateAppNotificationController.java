package com.ranchat.chatting.notification.controller;

import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.notification.service.UpdateAppNotificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/app-notifications")
@RequiredArgsConstructor
public class UpdateAppNotificationController {
    private final UpdateAppNotificationService service;

    @PutMapping
    ApiResponse<Void> update(@Valid @RequestBody Request request) {
        var command = new UpdateAppNotificationService.Command(
            request.userId(),
            request.agentId(),
            request.allowsNotification()
        );

        service.update(command);
        return ApiResponse.success();
    }

    record Request(
        @NotBlank(message = "userId는 필수입니다.")
        String userId,
        @NotBlank(message = "agentId는 필수입니다.")
        String agentId,
        @NotNull(message = "allowsNotification는 필수입니다.")
        Boolean allowsNotification
    ) {
    }
}