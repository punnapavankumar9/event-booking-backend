package com.punna.eventcore.service.impl;

import com.punna.eventcore.dto.UserDto;
import com.punna.eventcore.service.AuthService;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {

  private final String ROLE_PREFIX = "ROLE_";

  @Override
  public Mono<UserDto> getUserDto() {
    return ReactiveSecurityContextHolder
        .getContext()
        .map(m -> (UserDto) m
            .getAuthentication()
            .getPrincipal());
  }

  @Override
  public Mono<Boolean> hasRole(String role) {
    return getUserDto().map(user -> user
        .getAuthorities()
        .contains(ROLE_PREFIX + role));
  }

  @Override
  public Mono<String> getUserName() {
    return getUserDto().map(UserDto::getUsername);
  }
}
