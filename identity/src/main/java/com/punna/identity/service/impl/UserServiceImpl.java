package com.punna.identity.service.impl;

import com.punna.commons.exception.EntityNotFoundException;
import com.punna.identity.dto.UserRequestDto;
import com.punna.identity.dto.UserResponseDto;
import com.punna.identity.dto.UsernamePasswordDto;
import com.punna.identity.exception.InvalidUsernamePasswordCombination;
import com.punna.identity.exception.UserNameOrEmailExists;
import com.punna.identity.mapper.UserMapper;
import com.punna.identity.model.User;
import com.punna.identity.repository.UserRepository;
import com.punna.identity.service.UserService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @Override
  public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
        .orElseThrow(
            () -> new EntityNotFoundException(User.class.getSimpleName(), usernameOrEmail));
  }

  @Override
  public UserResponseDto createUser(UserRequestDto userRequestDto) {
    if (this.userRepository.existsByUsername(userRequestDto.getUsername())) {
      throw new UserNameOrEmailExists(userRequestDto.getUsername(), true);
    }
    if (this.userRepository.existsByEmail(userRequestDto.getEmail())) {
      throw new UserNameOrEmailExists(userRequestDto.getEmail(), false);
    }
    userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
    User savedUser = userRepository.save(UserMapper.toUser(userRequestDto));
    return UserMapper.toUserResponseDto(savedUser);
  }

  @Override
  public void createAdminUser(User user) {
    if (this.userRepository.existsByUsername(user.getUsername())) {
      throw new UserNameOrEmailExists(user.getUsername(), true);
    }
    if (this.userRepository.existsByEmail(user.getEmail())) {
      throw new UserNameOrEmailExists(user.getEmail(), false);
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
  }

  @Override
  public UserResponseDto updateUser(UserRequestDto userRequestDto) {
    String username = userRequestDto.getUsername();
    User savedUser = userRepository.findById(username)
        .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), username));
    UserMapper.merge(userRequestDto, savedUser);
    if (userRequestDto.getPassword() != null) {
      savedUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
    }
    savedUser = userRepository.save(savedUser);
    return UserMapper.toUserResponseDto(savedUser);
  }

  @Override
  public UserResponseDto findUserByUsernameOrEmail(String username) {
    User user = userRepository.findByUsernameOrEmail(username, username)
        .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), username));
    return UserMapper.toUserResponseDto(user);
  }

  @Override
  public void deleteUserByUsername(String username) {
    boolean exists = userRepository.existsById(username);
    if (!exists) {
      throw new EntityNotFoundException(User.class.getSimpleName(), username);
    }
    userRepository.deleteById(username);
  }

  @Override
  public String loginUser(UsernamePasswordDto usernamePasswordDto) {
    String usernameOrEmail = usernamePasswordDto.username();
    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
        .orElseThrow(
            () -> new EntityNotFoundException(User.class.getSimpleName(), usernameOrEmail));
    if (passwordEncoder.matches(usernamePasswordDto.password(), user.getPassword())) {
      updateLastLoginTime(user.getUsername());
      return jwtService.generateToken(user.getUsername());
    } else {
      throw new InvalidUsernamePasswordCombination(usernamePasswordDto.username());
    }
  }

  public UserResponseDto getUserByToken(String token) {
    String username = jwtService.parseUsername(token);
    if (username != null) {
      UserResponseDto userByUsernameOrEmail = findUserByUsernameOrEmail(username);
      if (userByUsernameOrEmail != null && userByUsernameOrEmail.isEnabled()) {
        return userByUsernameOrEmail;
      } else {
        throw new InvalidUsernamePasswordCombination(token);
      }
    } else {
      throw new InvalidUsernamePasswordCombination(token);
    }
  }

  @Override
  public void updateLastLoginTime(String username) {
    userRepository.updateLastLogin(username, Instant.now())
        .thenRun(
            () -> System.out.println("last login time for user:: " + username + " is updated"));
  }
}
