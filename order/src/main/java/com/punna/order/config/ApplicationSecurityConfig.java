package com.punna.order.config;


import com.punna.order.security.PopulateAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class ApplicationSecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      PopulateAuthentication populateAuthentication) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable).logout(AbstractHttpConfigurer::disable)
        .sessionManagement(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
        .addFilterAt(populateAuthentication, UsernamePasswordAuthenticationFilter.class).build();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return authentication -> {
      throw new UnsupportedOperationException(
          "Authentication is not supported here at downstream service.");
    };
  }
}
