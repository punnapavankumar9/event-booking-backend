package com.punna.gateway.config;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import jakarta.annotation.PostConstruct;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RetryCustomConfig {

  private final RetryRegistry retryRegistry;

  @PostConstruct
  public void init() {
    Predicate<Throwable> errorPredicate = t -> {
      log.error("Retry predicate with error: {}", t.getMessage());
      return t instanceof RuntimeException && (
          t.getMessage().equals("INTERNAL_SERVER_ERROR") || t.getMessage().equals("CLIENT_ERROR"));
    };

    Retry criticalRetry = retryRegistry.retry("critical");

    RetryConfig config = RetryConfig.from(criticalRetry.getRetryConfig())
        .retryOnException(errorPredicate).build();

    retryRegistry.replace("critical", Retry.of("critical", config));
    retryRegistry.replace("info", Retry.of("info", config));
  }

}
