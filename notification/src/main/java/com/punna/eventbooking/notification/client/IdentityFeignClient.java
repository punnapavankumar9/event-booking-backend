package com.punna.eventbooking.notification.client;

import com.punna.eventbooking.notification.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("identity-service")
public interface IdentityFeignClient {


  @GetMapping("/api/v1/users/{usernameOrEmail}")
  UserResponseDto findByUsernameOrEmail(@PathVariable String usernameOrEmail);

}
