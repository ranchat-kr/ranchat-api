package com.ranchat.chatting.common;

import com.ranchat.chatting.matching.repository.RedisMatchingRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Disabled
@SpringBootTest
@ActiveProfiles("dev")
public class SampleTest {
    @Autowired
    private RedisMatchingRepository matchingRepository;

    @Test
    void test() {
        var queue = matchingRepository.findWaitingUsers();
        matchingRepository.addUser("test1");
        matchingRepository.addUser("test2");
        var curQueue = matchingRepository.findWaitingUsers();
        System.out.println();
    }
}
