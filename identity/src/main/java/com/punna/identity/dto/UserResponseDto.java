package com.punna.identity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private String username;

    private String email;

    private boolean enabled = true;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private LocalDateTime lastLoginAt;
}
