package com.punna.eventcore.service;

import com.punna.eventcore.dto.UserDto;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<UserDto> getUserDto();

    Mono<Boolean> hasRole(String role);

    Mono<String> getUserName();

}
