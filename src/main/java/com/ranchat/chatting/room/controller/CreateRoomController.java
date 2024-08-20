package com.ranchat.chatting.room.controller;

import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.service.CreateRoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/rooms")
@RequiredArgsConstructor
public class CreateRoomController {
    private final CreateRoomService service;

    @PostMapping
    ApiResponse<Long> create(@Valid @RequestBody Request request) {
        return ApiResponse.success(
            service.create(request.toRequirement())
        );
    }

    record Request(
        @NotEmpty(message = "userIds 는 필수값입니다.")
        List<String> userIds,
        @NotNull(message = "roomType 은 필수값입니다.")
        ChatRoom.RoomType roomType,
        Optional<String> title
    ) {
        public CreateRoomService.Requirement toRequirement() {
            return new CreateRoomService.Requirement(
                userIds,
                title,
                roomType
            );
        }
    }
}
