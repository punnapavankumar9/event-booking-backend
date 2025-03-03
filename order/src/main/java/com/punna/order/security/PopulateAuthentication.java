package com.punna.order.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punna.order.dto.UserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@RequiredArgsConstructor
public class PopulateAuthentication extends OncePerRequestFilter {

  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String authHeader = "X-USER-DETAILS";
    if (request.getHeader(authHeader) != null) {
      authHeader = request.getHeader(authHeader);
      UserDto userDto = objectMapper.readValue(authHeader, UserDto.class);
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          userDto,
          null,
          userDto.getAuthorities() != null
              ? userDto.getAuthorities().stream()
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList())
              : List.of()
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    filterChain.doFilter(request, response);
  }
}
