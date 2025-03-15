package com.punna.identity.controller;

import com.punna.identity.dto.UserRequestDto;
import com.punna.identity.dto.UserResponseDto;
import com.punna.identity.dto.UsernamePasswordDto;
import com.punna.identity.mapper.UserMapper;
import com.punna.identity.model.User;
import com.punna.identity.service.UserService;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import com.punna.commons.validation.groups.CreateGroup;
import com.punna.commons.validation.groups.UpdateGroup;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping(value = "/login")
  public HashMap<String, String> login(
      @Validated @RequestBody UsernamePasswordDto usernamePasswordDto) {
    return new HashMap<>() {{
      put("token", userService.loginUser(usernamePasswordDto));
    }};
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponseDto signup(
      @Validated(CreateGroup.class) @RequestBody UserRequestDto userRequestDto) {
    return userService.createUser(userRequestDto);
  }

  @GetMapping("/getUsersDetailsByToken")
  public UserResponseDto getUserDetailsByToken(@RequestHeader("Authorization") String token) {
    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      throw new AccessDeniedException("Invalid Token");
    }
    return UserMapper.toUserResponseDto(
        (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

  @GetMapping("/{usernameOrEmail}")
  public UserResponseDto findByUsernameOrEmail(@PathVariable String usernameOrEmail) {
    return userService.findUserByUsernameOrEmail(usernameOrEmail);
  }

  @PatchMapping
  public UserResponseDto update(
      @Validated(UpdateGroup.class) @RequestBody UserRequestDto userRequestDto) {
    return userService.updateUser(userRequestDto);
  }

  @DeleteMapping("/{username}")
  public void delete(@PathVariable String username) {
    userService.deleteUserByUsername(username);
  }
}
