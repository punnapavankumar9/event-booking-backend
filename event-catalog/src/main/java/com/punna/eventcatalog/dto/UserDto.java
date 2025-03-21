package com.punna.eventcatalog.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private String username;

  private String email;

  private boolean enabled = true;

  private List<String> authorities = new ArrayList<>();

  private Instant createdAt;

  private Instant lastModifiedAt;

  private Instant lastLoginAt;
}
