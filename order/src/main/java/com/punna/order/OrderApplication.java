package com.punna.order;

import com.punna.order.dto.UserDto;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class OrderApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrderApplication.class, args);
  }

  @Bean
  public AuditorAware<String> auditorProvider() {
    return () -> {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication);
      UserDto principal = (UserDto) authentication.getPrincipal();
      return java.util.Optional.ofNullable(principal.getUsername());
    };
  }

  @Bean
  public Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }
}
