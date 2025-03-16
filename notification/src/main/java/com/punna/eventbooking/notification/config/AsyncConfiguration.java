package com.punna.eventbooking.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.async")
public class AsyncConfiguration {

  public Integer corePoolSize;

  public Integer maxPoolSize;

  public Integer queueCapacity;
}
