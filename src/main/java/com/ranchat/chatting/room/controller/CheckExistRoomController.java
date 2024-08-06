package com.ranchat.chatting.room.controller;

import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.room.service.CheckExistRoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/rooms/exists-by-userId")
@RequiredArgsConstructor
public class CheckExistRoomController {
    private final CheckExistRoomService service;

    @GetMapping
    ApiResponse<Boolean> check(@Valid Request request) {
        return ApiResponse.success(
            service.check(request.userId())
        );
    }

    record Request(
        @NotBlank(message = "userId는 필수입니다.")
        String userId
    ) {
    }
}
