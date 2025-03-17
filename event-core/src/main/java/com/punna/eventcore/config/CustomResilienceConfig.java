package com.punna.eventcore.config;

import com.punna.commons.exception.EventApplicationException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import jakarta.annotation.PostConstruct;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
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
      log.error("Circuit breaker error", t);
      if (t instanceof WebClientResponseException) {
        int statusCode = ((WebClientResponseException) t).getStatusCode().value();
        return statusCode != -1 && (statusCode < 400 || statusCode > 499);
      }
      if(t instanceof EventApplicationException){
        Integer status = ((EventApplicationException) t).getStatus();
        return status != -1 && (status < 400 || status > 499);
      }
      return false;
    };

    RetryConfig customRetryConfig = RetryConfig.from(config)
        .retryOnException(errorPredicate).build();

    // create retries
    retryRegistry.retry("countriesNow", customRetryConfig);
    retryRegistry.retry("catalog", customRetryConfig);

    // Create circuit Breakers
    CircuitBreaker countriesNow = circuitBreakerRegistry.circuitBreaker("countriesNow");
    CircuitBreaker catalog = circuitBreakerRegistry.circuitBreaker("catalog");

  }

}
