package com.ranchat.chatting.room.controller;

import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.room.service.GetRoomDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/rooms/{roomId}")
@RequiredArgsConstructor
public class GetRoomDetailsController {
    private final GetRoomDetailsService service;

    @GetMapping
    ApiResponse<GetRoomDetailsService.RoomDetails> get(@PathVariable Long roomId) {
        return ApiResponse.success(
            service.get(roomId)
        );
    }
}
