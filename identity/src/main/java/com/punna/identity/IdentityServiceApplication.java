package com.punna.identity;

import com.punna.identity.model.User;
import com.punna.identity.service.UserService;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableJpaAuditing
@EnableAsync
@SpringBootApplication
public class IdentityServiceApplication implements ApplicationContextAware {


  public static void main(String[] args) {
    SpringApplication.run(IdentityServiceApplication.class, args);
  }

  // create Admin user
  @EventListener(ApplicationReadyEvent.class)
  @ConditionalOnProperty(prefix = "application", value = "mode", havingValue = "dev")
  public void init() {
    UserService userService = this.applicationContext.getBean(UserService.class);
    try {
      userService.findUserByUsernameOrEmail("punna");
    } catch (Exception e) {
      userService.createAdminUser(
          User.builder().username("punna").password("password").email("punnapavankumar9@gmail.com")
              .enabled(true)
              .authorities(List.of(
                  new SimpleGrantedAuthority("ROLE_ADMIN"),
                  new SimpleGrantedAuthority("ROLE_SUPER_USER"))).build());
    }
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
