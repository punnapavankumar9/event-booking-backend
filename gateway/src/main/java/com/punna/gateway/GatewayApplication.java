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
    return builder.routes()
        .route("identity-service", p -> p.path("/api/v*/users/**").uri("lb://IDENTITY-SERVICE"))
        .route("event-catalog",
            p -> p.path("/api/v*/venues/**", "/api/v*/events/**", "/api/v*/seating-layout/**")
                .filters(f -> f.filter(authFilter)).uri("lb://EVENT-CORE-SERVICE"))
        .route("event-catalog", p -> p.path("/api/v*/movies/**").filters(f -> f.filter(authFilter))
            .uri("lb://EVENT-CATALOG-SERVICE")).route("event-catalog",
            p -> p.path("/assets/movie-catalog/**").uri("lb://EVENT-CATALOG-SERVICE")).build();
  }
}
