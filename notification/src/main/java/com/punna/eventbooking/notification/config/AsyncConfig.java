package com.punna.eventbooking.notification.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@RequiredArgsConstructor
public class AsyncConfig {

  private final AsyncConfiguration asyncConfiguration;

  @Bean(name = "emailTaskExecutor")
  ThreadPoolTaskExecutor emailTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    executor.setMaxPoolSize(asyncConfiguration.maxPoolSize);
    executor.setCorePoolSize(asyncConfiguration.corePoolSize);
    executor.setQueueCapacity(asyncConfiguration.maxPoolSize);
    executor.setThreadNamePrefix("EmailTaskExecutor-");
    executor.initialize();
    return executor;
  }
}
