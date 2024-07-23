package com.ranchat.chatting.config;

import com.ranchat.chatting.exception.SchedulerErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@EnableScheduling
@Configuration
public class SchedulerConfiguration implements SchedulingConfigurer {
    private final int poolSize;
    private final SchedulerErrorHandler schedulerErrorHandler;

    public SchedulerConfiguration(@Value("${scheduler.pool-size}") int poolSize,
                                  SchedulerErrorHandler schedulerErrorHandler) {
        this.poolSize = poolSize;
        this.schedulerErrorHandler = schedulerErrorHandler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        var threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(poolSize);
        threadPoolTaskScheduler.setThreadNamePrefix("scheduler-task-pool-");
        threadPoolTaskScheduler.setErrorHandler(schedulerErrorHandler);
        threadPoolTaskScheduler.initialize();

        scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}
