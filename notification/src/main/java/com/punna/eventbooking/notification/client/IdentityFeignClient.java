package com.punna.eventbooking.notification.client;

import com.punna.eventbooking.notification.dto.UserResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("identity-service")
public interface IdentityFeignClient {


  @Retry(name = "identity")
  @CircuitBreaker(name = "identity")
  @GetMapping("/api/v1/users/{usernameOrEmail}")
  UserResponseDto findByUsernameOrEmail(@PathVariable String usernameOrEmail);

}
