package com.punna.identity.security;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.punna.identity.service.impl.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      JwtRequestFilter jwtRequestFilter,
      CustomOAuth2UserService customOAuth2UserService,
      CustomOauth2LoginSuccessHandler customOauth2LoginSuccessHandler) throws Exception {
    AuthenticationEntryPoint unauthorizedHandler = (request, response, authException) -> {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
          "Unauthorized: Invalid or missing JWT token");
    };
    return http.csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .oauth2Login(oauth2loginConfigurer ->
            oauth2loginConfigurer.userInfoEndpoint(
                    userInfoEndpointConfig -> userInfoEndpointConfig.userService(
                        customOAuth2UserService))
                .successHandler(customOauth2LoginSuccessHandler))
        .httpBasic(AbstractHttpConfigurer::disable)
        .sessionManagement(AbstractHttpConfigurer::disable)
        .exceptionHandling(exceptionHandlingConfigurer ->
            exceptionHandlingConfigurer.authenticationEntryPoint(unauthorizedHandler))
        .authorizeHttpRequests(
            authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry.requestMatchers(
                    HttpMethod.GET, "/api/v1/users/getUsersDetailsByToken").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/v1/users/**", "/actuator/health").permitAll()
                .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/users"),
                    antMatcher(HttpMethod.POST, "/api/v1/users/login"),
                    antMatcher(HttpMethod.OPTIONS, "/**"),
                    antMatcher("/v3/api-docs/**"), antMatcher("/swagger-ui/**")).permitAll()
                .anyRequest().authenticated())
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class).build();
  }
}
