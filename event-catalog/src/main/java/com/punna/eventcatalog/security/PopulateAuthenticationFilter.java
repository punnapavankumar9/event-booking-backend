package com.punna.eventcatalog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punna.eventcatalog.dto.UserDto;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PopulateAuthenticationFilter implements WebFilter {

  private final ObjectMapper objectMapper;

  @SneakyThrows
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String authHeader = "X-USER-DETAILS";
    if (exchange.getRequest().getHeaders().get(authHeader) != null) {
      UserDto userDto = objectMapper.readValue(
          exchange.getRequest().getHeaders().getFirst(authHeader), UserDto.class);
      return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(
          new UsernamePasswordAuthenticationToken(userDto, null,
              userDto.getAuthorities() != null ? userDto.getAuthorities().stream()
                  .map(SimpleGrantedAuthority::new).toList() : new ArrayList<>())));
    }
    return chain.filter(exchange);
  }
}
