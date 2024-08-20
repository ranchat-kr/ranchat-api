package com.ranchat.chatting.room.controller;

import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.service.CreateRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
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
    ApiResponse<Long> create(Request request) {
        return ApiResponse.success(
            service.create(request.toRequirement())
        );
    }

    record Request(
        List<String> userIds,
        Optional<String> title,
        ChatRoom.RoomType roomType
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
