package com.ranchat.chatting.user.controller;

import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.user.service.CreateUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class CreateUserController {
    private final CreateUserService service;

    @PostMapping
    ApiResponse<Void> create(@Valid @RequestBody Request request) {
        service.create(request.toRequirement());

        return ApiResponse.success();
    }

    record Request(
        @NotBlank(message = "아이디는 필수값입니다.")
        String id,
        @NotBlank(message = "이름은 필수값입니다.")
        String name
    ) {
        public CreateUserService.Requirement toRequirement() {
            return new CreateUserService.Requirement(
                id,
                name
            );
        }
    }
}
