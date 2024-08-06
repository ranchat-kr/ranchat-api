package com.ranchat.chatting.room.controller;

import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.room.service.ExitRoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/rooms/{roomId}/exit")
@RequiredArgsConstructor
public class ExitRoomController {
    private final ExitRoomService service;

    @PostMapping
    ApiResponse<Void> exit(@PathVariable Long roomId,
                           @Valid @RequestBody Request request) {
        service.exit(roomId, request.userId());
        return ApiResponse.success();
    }

    record Request(
        @NotBlank(message = "userId는 필수입니다.")
        String userId
    ) {
    }
}
