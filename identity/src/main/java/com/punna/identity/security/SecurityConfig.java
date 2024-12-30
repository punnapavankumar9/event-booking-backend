package com.punna.identity.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    @ConditionalOnExpression("false == false ")
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedOrigins("http://localhost:8080")
                        .allowedMethods("HEAD", "PUT", "PATCH", "POST", "GET", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtRequestFilter jwtRequestFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/users"),
                                antMatcher(HttpMethod.POST, "/api/v1/users/login"),
                                antMatcher(HttpMethod.OPTIONS, "/**"),
                                antMatcher("/v3/api-docs/**"),
                                antMatcher("/swagger-ui/**"))
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
