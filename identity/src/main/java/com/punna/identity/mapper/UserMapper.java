package com.punna.identity.mapper;

import com.punna.identity.dto.UserRequestDto;
import com.punna.identity.dto.UserResponseDto;
import com.punna.identity.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserMapper {

    public static User toUser(final UserRequestDto user) {
        return User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .authorities((user
                        .getAuthorities()
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList()))
                .enabled(user.isEnabled())
                .build();
    }

    public static UserResponseDto toUserResponseDto(final User user) {
        return UserResponseDto
                .builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(user
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .createdAt(user.getCreatedAt())
                .lastModifiedAt(user.getLastModifiedAt())
                .lastLoginAt(user.getLastLoginAt())
                .enabled(user.isEnabled())
                .build();
    }

    public static void merge(final UserRequestDto userRequestDto, final User user) {
        if (userRequestDto.getUsername() != null) {
            user.setUsername(userRequestDto.getUsername());
        }
        if (userRequestDto.getEmail() != null) {
            user.setEmail(userRequestDto.getEmail());
        }
        if (userRequestDto.getAuthorities() != null) {
            user
                    .getAuthorities()
                    .addAll(userRequestDto
                            .getAuthorities()
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList());
        }
        if (userRequestDto.isEnabled() != user.isEnabled()) {
            user.setEnabled(userRequestDto.isEnabled());
        }
    }

}
