package com.punna.gateway.filter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.punna.gateway.service.AuthService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class AuthFilter implements GatewayFilter {

  private final AuthService authService;
  private final ObjectMapper objectMapper;


  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String XUserDetailsHeader = "X-USER-DETAILS";
    if (exchange.getRequest().getHeaders().containsKey(XUserDetailsHeader)) {
      // Set the response status to 400 Bad Request
      exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

      // Optionally, set a response body with an error message
      return Mono.just(exchange.getResponse())
          .flatMap(response -> {
            response.getHeaders().setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            return response.writeWith(Mono.just(response.bufferFactory()
                .wrap("{\"error\": \"X-USER-DETAILS header is not allowed\"}".getBytes())));
          });
    }

    return Mono.fromSupplier(() -> {
      List<String> authorizationHeaders = exchange.getRequest().getHeaders()
          .get("Authorization");
      if (authorizationHeaders == null) {
        return chain.filter(exchange);
      }
      String authHeader = authorizationHeaders.getFirst();
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return chain.filter(exchange);
      } else {
        return authService.getLoggedInUser(authHeader).flatMap(e -> {
          ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
          try {
            requestBuilder.header(XUserDetailsHeader, objectMapper.writeValueAsString(e));
          } catch (JsonProcessingException ex) {
            return Mono.error(new RuntimeException(ex));
          }
          return chain.filter(exchange.mutate().request(requestBuilder.build()).build());
        }).switchIfEmpty(chain.filter(exchange));
      }
    }).flatMap(m -> m);
  }
}
