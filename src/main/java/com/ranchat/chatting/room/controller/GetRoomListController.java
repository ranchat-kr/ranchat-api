package com.ranchat.chatting.room.controller;

import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.common.web.PageResponse;
import com.ranchat.chatting.room.service.GetRoomListService;
import com.ranchat.chatting.room.vo.ChatRoomSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1/rooms")
@RequiredArgsConstructor
public class GetRoomListController {
    private final GetRoomListService service;

    @GetMapping
    ApiResponse<PageResponse<ChatRoomSummary>> get(Request request,
                                                   @PageableDefault Pageable pageable) {
        var serviceRequest = new GetRoomListService.Request(
            pageable,
            request.userId()
        );

        return ApiResponse.success(
            PageResponse.from(
                service.get(serviceRequest)
            )
        );
    }

    record Request(
        Optional<String> userId
    ) {
    }
}
