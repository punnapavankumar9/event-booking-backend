package com.punna.gateway.service;

import com.punna.gateway.dto.LoggedInUserDto;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AuthService {


    private final WebClient webClient;

    public AuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080")
                .build();
    }

    public Mono<LoggedInUserDto> getLoggedInUser(String header) {
        return webClient.get()
                .uri("/api/v1/users/getUsersDetailsByToken")
                .header(HttpHeaders.AUTHORIZATION, header)
                .retrieve()
                .onStatus(httpStatusCode -> !httpStatusCode.is2xxSuccessful(), clientResponse -> {
                            if (clientResponse.statusCode()
                                    .is4xxClientError()) {
                                return Mono.error(() -> new RuntimeException("CLIENT_ERROR"));
                            } else {
                                return Mono.error(new RuntimeException("INTERNAL_SERVER_ERROR"));
                            }
                        }
                         )
                .bodyToMono(LoggedInUserDto.class);
    }
}
