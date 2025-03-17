package com.punna.gateway.service;

import com.punna.gateway.dto.LoggedInUserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthService {


  private final WebClient webClient;

  public AuthService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
  }

  @CircuitBreaker(name = "critical")
  @Retry(name = "critical", fallbackMethod = "fallback")
  public Mono<LoggedInUserDto> getLoggedInUser(String header) {
    return webClient.get().uri("/api/v1/users/getUsersDetailsByToken")
        .header(HttpHeaders.AUTHORIZATION, header).retrieve()
        .onStatus(httpStatusCode -> !httpStatusCode.is2xxSuccessful(), clientResponse -> {
          if (clientResponse.statusCode() == HttpStatus.UNAUTHORIZED
              || clientResponse.statusCode() == HttpStatus.FORBIDDEN) {
            log.info("Unauthorized || forbidden");
            return Mono.empty();
          } else if (clientResponse.statusCode().is4xxClientError()) {
            log.info("4xxClientError");
            return Mono.error(() -> new RuntimeException("CLIENT_ERROR"));
          } else {
            log.info("error: {}", clientResponse.statusCode());
            return Mono.error(() -> new RuntimeException("INTERNAL_SERVER_ERROR"));
          }
        }).bodyToMono(LoggedInUserDto.class);
  }

  public Mono<Void> fallback(String header, Throwable throwable) {
    log.info("Fallback triggered with error: {}", throwable.getMessage());
    return Mono.empty();
  }
}
