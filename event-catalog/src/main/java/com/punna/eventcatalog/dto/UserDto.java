package com.punna.eventcatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String username;

    private String email;

    private boolean enabled = true;

    private List<String> authorities = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private LocalDateTime lastLoginAt;
}
