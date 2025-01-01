package com.punna.eventcatalog.config;

import com.punna.eventcatalog.security.PopulateAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

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
                        .pathMatchers(HttpMethod.GET, "/api/v1/events/**", "/api/v1/venues/**", "/api/v1/seating-arrangement/**")
                        .permitAll()
                        .pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        // centralized swagger
                        .pathMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()
                        .anyExchange()
                        .authenticated())
                .addFilterAt(populateAuthentication, SecurityWebFiltersOrder.HTTP_BASIC)
                .build();
    }
}
