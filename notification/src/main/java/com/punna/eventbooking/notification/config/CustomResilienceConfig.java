package com.punna.eventbooking.notification.config;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import jakarta.annotation.PostConstruct;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomResilienceConfig {

  private final RetryRegistry retryRegistry;

  private final CircuitBreakerRegistry circuitBreakerRegistry;

  @PostConstruct
  public void init() {

    // Retry
    Retry criticalRetry = retryRegistry.retry("info");
    RetryConfig config = criticalRetry.getRetryConfig();
    Predicate<Throwable> errorPredicate = (t) -> {
      if (t instanceof FeignException) {
        int statusCode = ((FeignException) t).status();
        return statusCode != -1 && (statusCode < 400 || statusCode > 499);
      }
      return false;
    };

    RetryConfig customRetryConfig = RetryConfig.from(config)
        .retryOnException(errorPredicate).build();

    // create retries
    retryRegistry.retry("eventCore", customRetryConfig);
    retryRegistry.retry("identity", customRetryConfig);

    // Create circuit Breakers
    CircuitBreaker identity = circuitBreakerRegistry.circuitBreaker("identity");
    CircuitBreaker eventCore = circuitBreakerRegistry.circuitBreaker("eventCore");

  }


}
