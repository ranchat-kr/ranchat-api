package com.ranchat.chatting.user.service;

import com.ranchat.chatting.common.support.Status;
import com.ranchat.chatting.exception.BadRequestException;
import com.ranchat.chatting.user.repository.UserRepository;
import com.ranchat.chatting.user.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ranchat.chatting.common.support.Status.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GetUserDetailsService {
    private final UserRepository userRepository;

    public UserVo get(String userId) {
        return userRepository.findById(userId)
            .map(UserVo::from)
            .orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
    }
}
