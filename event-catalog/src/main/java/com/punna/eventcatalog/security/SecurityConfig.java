package com.punna.eventcatalog.security;

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
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
        PopulateAuthenticationFilter populateAuthenticationFilter) {
        return http.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .csrf(ServerHttpSecurity.CsrfSpec::disable).authorizeExchange(
                p -> p.pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .pathMatchers(HttpMethod.GET, "/assets/**").permitAll()
                    .pathMatchers(HttpMethod.GET, "/api/v1/movies/**").permitAll().anyExchange()
                    .authenticated())
            .addFilterAt(populateAuthenticationFilter, SecurityWebFiltersOrder.HTTP_BASIC).build();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        return (authentication) -> Mono.error(new UnsupportedOperationException(
            "Authentication is not supported here at downstream service."));
    }


}
