package com.punna.eventcatalog.service;

import com.punna.eventcatalog.dto.UserDto;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<UserDto> getUserDto();

    Mono<Boolean> hasRole(String role);

    Mono<String> getUserName();

}
