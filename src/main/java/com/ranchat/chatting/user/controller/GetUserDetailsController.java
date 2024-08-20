package com.ranchat.chatting.user.controller;

import com.ranchat.chatting.common.web.ApiResponse;
import com.ranchat.chatting.user.service.GetUserDetailsService;
import com.ranchat.chatting.user.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users/{userId}")
@RequiredArgsConstructor
public class GetUserDetailsController {
    private final GetUserDetailsService service;

    @GetMapping
    ApiResponse<UserVo> get(@PathVariable String userId){
        return ApiResponse.success(
            service.get(userId)
        );
    }
}
