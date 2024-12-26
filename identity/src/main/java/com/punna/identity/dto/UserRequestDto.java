package com.punna.identity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.punna.commons.validation.groups.CreateGroup;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserRequestDto {
    @NotNull(message = "username must not be null")
    private String username;

    @NotNull(message = "user password must not be null", groups = CreateGroup.class)
    @Size(min = 8, max = 50, message = "Password length must be in between 8 and 50 characters")
    private String password;

    @NotNull(message = "email must not be null", groups = CreateGroup.class)
    @Email(message = "email format is invalid")
    private String email;

    private boolean enabled = true;

    private List<String> authorities = new ArrayList<>();
}
