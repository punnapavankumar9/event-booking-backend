package com.punna.gateway.dto;

import java.time.LocalDateTime;
import java.util.List;

public record LoggedInUserDto(String username,

                              String email,

                              boolean enabled,

                              List<String> authorities,

                              LocalDateTime createdAt,

                              LocalDateTime lastModifiedAt,

                              LocalDateTime lastLoginAt

) {


}
