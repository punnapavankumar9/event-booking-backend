package com.punna.gateway;

import com.punna.gateway.filter.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, AuthFilter authFilter) {
        return builder
                .routes()
                .route("identity-service",
                        p -> p
                                .path("/api/v*/users/**")
                                .uri("lb://IDENTITY-SERVICE"))
                .route("event-catalog",
                        p -> p
                                .path("/event-catalog/**")
                                .filters(f -> f.filter(authFilter))
                                .uri("lb://EVENT-CATALOG-SERVICE"))
                .build();
    }


}
