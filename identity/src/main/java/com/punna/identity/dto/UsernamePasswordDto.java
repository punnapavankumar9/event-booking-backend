package com.punna.identity.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsernamePasswordDto(

        @NotNull(message = "username must not be null") String username,

        @NotNull(message = "user password must not be null") @Size(min = 8, max = 50,
                message = "Password length must be in between 8 and 50 characters") String password

) {

}
