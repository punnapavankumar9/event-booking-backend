package com.punna.identity.mapper;

import com.punna.identity.dto.UserRequestDto;
import com.punna.identity.dto.UserResponseDto;
import com.punna.identity.model.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserMapper {

  public static User toUser(final UserRequestDto user) {
    return User
        .builder()
        .username(user.getUsername())
        .password(user.getPassword())
        .email(user.getEmail())
        .enabled(user.isEnabled())
        .build();
  }

  public static UserResponseDto toUserResponseDto(final User user) {
    List<String> authorities = List.of();
    if(user.getAuthorities() != null && !user.getAuthorities().isEmpty()) {
      authorities = user.getAuthorities().stream().map(SimpleGrantedAuthority::getAuthority).toList();
    }
    return UserResponseDto
        .builder()
        .username(user.getUsername())
        .email(user.getEmail())
        .createdAt(user.getCreatedAt())
        .lastModifiedAt(user.getLastModifiedAt())
        .lastLoginAt(user.getLastLoginAt())
        .enabled(user.isEnabled())
        .provider(user.getProvider())
        .providerId(user.getProviderId())
        .authorities(authorities)
        .build();
  }

  public static void merge(final UserRequestDto userRequestDto, final User user) {
    if (userRequestDto.getUsername() != null) {
      user.setUsername(userRequestDto.getUsername());
    }
    if (userRequestDto.getEmail() != null) {
      user.setEmail(userRequestDto.getEmail());
    }
    if (userRequestDto.isEnabled() != user.isEnabled()) {
      user.setEnabled(userRequestDto.isEnabled());
    }
  }

}
