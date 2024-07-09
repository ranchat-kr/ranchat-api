package com.ranchat.chatting.user.controller;

import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.user.service.UpdateUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users/{userId}")
@RequiredArgsConstructor
public class UpdateUserController {
    private final UpdateUserService service;

    @PutMapping
    ApiResponse<Void> update(@PathVariable String userId,
                             @Valid @RequestBody Request request) {
        service.update(request.toRequirement(userId));

        return ApiResponse.success();
    }

    record Request(
        @NotBlank(message = "이름은 필수값입니다.")
        String name
    ) {
        public UpdateUserService.Requirement toRequirement(String userId) {
            return new UpdateUserService.Requirement(
                userId,
                name
            );
        }
    }
}
