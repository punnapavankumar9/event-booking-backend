package com.punna.identity.security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerSecurityConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-key",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("jwt")
                                                               ))
                .info(new Info()
                        .title("Identity Security API")
                        .version("1.0")
                        .contact(new Contact()
                                .email("pavankumar.punna@outlook.com")
                                .name("Pavan Kumar Punna"))
                        .description("Identity Service for Event booking platform"))
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
    }
}
