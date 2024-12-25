package com.punna.identity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto extends UserRequestDto {

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private LocalDateTime lastLoginAt;
}
