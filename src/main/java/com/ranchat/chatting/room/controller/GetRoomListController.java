package com.ranchat.chatting.room.controller;

import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.room.service.GetRoomListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/rooms")
@RequiredArgsConstructor
public class GetRoomListController {
    private final GetRoomListService service;

    @GetMapping
    ApiResponse<List<GetRoomListService.RoomSummary>> get() {
        return ApiResponse.success(
            service.get()
        );
    }
}
