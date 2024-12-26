package com.punna.identity.fixtures;

import com.punna.identity.dto.UserRequestDto;

import java.util.List;

public class TestFixtures {

    public static final UserRequestDto SAMPLE_USER_DTO = UserRequestDto
            .builder()
            .email("pavan@gmail.com")
            .username("pavan")
            .password("password")
            .enabled(true)
            .authorities(List.of("ROLE_USER", "ROLE_ADMIN"))
            .build();

}
