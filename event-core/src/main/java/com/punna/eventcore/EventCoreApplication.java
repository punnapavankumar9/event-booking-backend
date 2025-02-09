package com.punna.eventcore;

import com.punna.eventcore.dto.UserDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

@SpringBootApplication
@EnableReactiveMongoAuditing
public class EventCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventCoreApplication.class, args);
    }

    @Bean
    public ReactiveAuditorAware<String> auditorProvider() {
        return () -> ReactiveSecurityContextHolder
                .getContext()
                .map(SecurityContext::getAuthentication)
                .map(auth -> ((UserDto) auth.getPrincipal()).getUsername());
    }
}
