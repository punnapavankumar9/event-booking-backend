package com.punna.gateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class CircuitBreakerCustomConfig {

  @Bean
  public Customizer<ReactiveResilience4JCircuitBreakerFactory> circuitBreakerFactoryCustomizer() {
    return (factory) -> factory.configure(builder -> {
      log.info("Circuit breaker factory configured");
      CircuitBreakerConfig customConfig = CircuitBreakerConfig.custom()
          .recordException(ex -> {
            log.error("Customized Circuit breaker exception", ex);
            return ex instanceof RuntimeException && (ex.getMessage()
                .equals("INTERNAL_SERVER_ERROR") || ex.getMessage().equals("CLIENT_ERROR"));
          })
          .build();
      builder.circuitBreakerConfig(customConfig).build();
    }, "critical", "info");
  }
}
