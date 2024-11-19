package com.ranchat.chatting.room.controller;

import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.common.web.PageResponse;
import com.ranchat.chatting.room.service.GetJoinedRoomListService;
import com.ranchat.chatting.room.vo.ChatRoomSummary;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/rooms")
@RequiredArgsConstructor
public class GetJoinedRoomListController {
    private final GetJoinedRoomListService service;

    @GetMapping
    ApiResponse<PageResponse<ChatRoomSummary>> get(Request request,
                                                   @PageableDefault Pageable pageable) {
        var serviceRequest = new GetJoinedRoomListService.Request(
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
        @NotBlank(message = "userId 는 필수값입니다.")
        String userId
    ) {
    }
}