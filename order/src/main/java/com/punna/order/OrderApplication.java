package com.punna.order;

import com.punna.order.dto.UserDto;
import feign.Logger;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
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
      if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
        // as authentication is enforced at controller layer kafka events or any service call should get dummy.
        return Optional.of("SERVICE_ACCOUNT_OR_KAFKA");
      }
      Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication);
      UserDto principal = (UserDto) authentication.getPrincipal();
      return java.util.Optional.ofNullable(principal.getUsername());
    };
  }

  @Bean
  public Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }

  @Autowired
  Environment environment;

  @PostConstruct
  public void logAllConfigs() {
    System.out.println("🚀 All Fetched Configurations:");
    Arrays.stream(environment.getActiveProfiles())
        .forEach(profile -> System.out.println("🔹 Active Profile: " + profile));

    System.out.println("🔹 spring.kafka.bootstrap-servers: " + environment.getProperty(
        "spring.kafka.bootstrap-servers"));
    System.out.println("🔹 spring.kafka.producer.value-serializer: " + environment.getProperty(
        "spring.kafka.producer.value-serializer"));
  }

}
