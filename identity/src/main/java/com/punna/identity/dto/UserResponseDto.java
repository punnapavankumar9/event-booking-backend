package com.punna.identity.dto;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

  private String username;

  private String email;

  private boolean enabled = true;

  private Instant createdAt;

  private Instant lastModifiedAt;

  private Instant lastLoginAt;

  private String providerId;

  private OAuthProvider provider;

  private List<String> authorities;
}
