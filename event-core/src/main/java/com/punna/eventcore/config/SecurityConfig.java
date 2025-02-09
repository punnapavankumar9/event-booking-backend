package com.punna.eventcore.config;

import com.punna.eventcore.security.PopulateAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain webFilterChain(ServerHttpSecurity http,
                                                 PopulateAuthentication populateAuthentication) {
        return http
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(p -> p
                        .pathMatchers(HttpMethod.GET,
                                "/api/v1/events/**",
                                "/api/v1/venues/**",
                                "/api/v1/seating-layout/**"
                                     )
                        .permitAll()
                        .pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        // for web browsers
                        .pathMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()
                        .anyExchange()
                        .authenticated())
                .addFilterAt(populateAuthentication, SecurityWebFiltersOrder.HTTP_BASIC)
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return authentication -> Mono.error(new UnsupportedOperationException(
                "Authentication is not supported here at downstream service."));
    }
}
