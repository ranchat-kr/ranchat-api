package com.ranchat.chatting.room.controller;

import com.ranchat.chatting.common.support.Status;
import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.exception.BadRequestException;
import com.ranchat.chatting.room.service.GetRoomDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/rooms/{roomId}")
@RequiredArgsConstructor
public class GetRoomDetailsController {
    private final GetRoomDetailsService service;

    @GetMapping
    ApiResponse<GetRoomDetailsService.RoomDetails> get(@PathVariable Long roomId,
                                                       @RequestParam(required = false) String userId){
        if (userId != null && userId.isBlank()) {
            throw new BadRequestException(Status.INVALID_PARAMETER, "userId는 공백일 수 없습니다.");
        }

        return ApiResponse.success(
            service.get(roomId, Optional.ofNullable(userId))
        );
    }
}
