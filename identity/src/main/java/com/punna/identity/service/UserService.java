package com.punna.identity.service;

import com.punna.identity.dto.UserRequestDto;
import com.punna.identity.dto.UserResponseDto;
import com.punna.identity.dto.UsernamePasswordDto;
import com.punna.identity.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserResponseDto createUser(UserRequestDto userRequestDto);

    void createAdminUser(User user);

    UserResponseDto updateUser(UserRequestDto userRequestDto);

    UserResponseDto findUserByUsernameOrEmail(String username);

    UserResponseDto getUserByToken(String token);

    void deleteUserByUsername(String username);

    String loginUser(UsernamePasswordDto usernamePasswordDto);

    void updateLastLoginTime(String username);
}
