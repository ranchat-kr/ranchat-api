package com.ranchat.chatting.message.controller;

import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.common.web.PageResponse;
import com.ranchat.chatting.message.service.GetMessageListService;
import com.ranchat.chatting.message.vo.ChatMessageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/rooms/{roomId}/messages")
@RequiredArgsConstructor
public class GetMessageListController {
    private final GetMessageListService service;

    @GetMapping
    ApiResponse<PageResponse<ChatMessageVo>> get(@PathVariable Long roomId,
                                                 @PageableDefault Pageable pageable,
                                                 Request request) {
        var requirement = new GetMessageListService.Requirement(
            roomId,
            request.createdAt(),
            pageable
        );
        var pageResponse = PageResponse.from(
            service.get(requirement)
        );

        return ApiResponse.success(pageResponse);
    }

    record Request(
        Optional<LocalDateTime> createdAt
    ) {
    }
}
