package com.punna.identity.controller;

import com.punna.identity.dto.UserRequestDto;
import com.punna.identity.dto.UserResponseDto;
import com.punna.identity.dto.UsernamePasswordDto;
import com.punna.identity.mapper.UserMapper;
import com.punna.identity.model.User;
import com.punna.identity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.punna.commons.validation.groups.CreateGroup;
import org.punna.commons.validation.groups.UpdateGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto signup(@Validated(CreateGroup.class) @RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @GetMapping("/getUsersDetailsByToken")
    public UserResponseDto getUserDetailsByToken(@RequestHeader("Authorization") String token) {
        return UserMapper.toUserResponseDto((User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
    }

    @GetMapping("/{usernameOrEmail}")
    public UserResponseDto findByUsernameOrEmail(@PathVariable String usernameOrEmail) {
        return userService.findUserByUsernameOrEmail(usernameOrEmail);
    }

    @PatchMapping
    public UserResponseDto update(@Validated(UpdateGroup.class) @RequestBody UserRequestDto userRequestDto) {
        return userService.updateUser(userRequestDto);
    }

    @DeleteMapping("/{username}")
    public void delete(@PathVariable String username) {
        userService.deleteUserByUsername(username);
    }

    @PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
    public String login(@Validated @RequestBody UsernamePasswordDto usernamePasswordDto) {
        return userService.loginUser(usernamePasswordDto);
    }
}
