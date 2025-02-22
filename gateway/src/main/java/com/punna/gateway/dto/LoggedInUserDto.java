package com.punna.gateway.dto;

import java.time.Instant;
import java.util.List;

public record LoggedInUserDto(String username,

                              String email,

                              boolean enabled,

                              List<String> authorities,

                              Instant createdAt,

                              Instant lastModifiedAt,

                              Instant lastLoginAt

) {


}
