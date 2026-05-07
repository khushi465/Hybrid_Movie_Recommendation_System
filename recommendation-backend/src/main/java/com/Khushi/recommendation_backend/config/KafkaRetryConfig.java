package com.Khushi.recommendation_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class KafkaRetryConfig {

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {

        ThreadPoolTaskScheduler scheduler =
                new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("kafka-retry-");
        scheduler.initialize();

        return scheduler;
    }
}