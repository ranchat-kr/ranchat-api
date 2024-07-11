package com.ranchat.chatting.common.support;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncExecutor {

    @Async
    public void execute(Runnable task) {
        task.run();
    }
}
