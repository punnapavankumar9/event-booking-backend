package com.punna.gateway.filter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.punna.gateway.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AuthFilter implements GatewayFilter {

    private final AuthService authService;
    private final ObjectMapper objectMapper;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono
                .fromSupplier(() -> {
                    List<String> authorizationHeaders = exchange
                            .getRequest()
                            .getHeaders()
                            .get("Authorization");
                    if (authorizationHeaders == null) {
                        return chain.filter(exchange);
                    }
                    String authHeader = authorizationHeaders.getFirst();
                    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                        return chain.filter(exchange);
                    } else {
                        return authService
                                .getLoggedInUser(authHeader)
                                .flatMap(e -> {
                                    ServerHttpRequest.Builder requestBuilder = exchange
                                            .getRequest()
                                            .mutate();
                                    try {
                                        requestBuilder.header("X-USER_DETAILS", objectMapper.writeValueAsString(e));
                                    } catch (JsonProcessingException ex) {
                                        return Mono.error(new RuntimeException(ex));
                                    }
                                    return chain.filter(exchange
                                            .mutate()
                                            .request(requestBuilder.build())
                                            .build());
                                });
                    }
                })
                .flatMap(m -> m);
    }
}
