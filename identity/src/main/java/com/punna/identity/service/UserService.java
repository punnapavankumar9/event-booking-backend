package com.punna.identity.service;

import com.punna.identity.dto.UserRequestDto;
import com.punna.identity.dto.UserResponseDto;
import com.punna.identity.dto.UsernamePasswordDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto updateUser(UserRequestDto userRequestDto);

    UserResponseDto findUserByUsernameOrEmail(String username);

    void deleteUserByUsername(String username);

    String loginUser(UsernamePasswordDto usernamePasswordDto);

    void updateLastLoginTime(String username);
}
