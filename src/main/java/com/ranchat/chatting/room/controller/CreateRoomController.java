package com.ranchat.chatting.room.controller;

import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.room.domain.ChatRoom;
import com.ranchat.chatting.room.service.CreateRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/rooms")
@RequiredArgsConstructor
public class CreateRoomController {
    private final CreateRoomService service;

    @PostMapping
    ApiResponse<Void> create(Request request) {
        service.create(request.toRequirement());

        return ApiResponse.success();
    }

    record Request(
        String userId,
        String title,
        ChatRoom.RoomType roomType
    ) {
        public CreateRoomService.Requirement toRequirement() {
            return new CreateRoomService.Requirement(
                List.of(userId),
                title,
                roomType
            );
        }
    }
}
