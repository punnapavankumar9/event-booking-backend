package com.punna.eventcore.client;

import com.punna.commons.exception.EventApplicationException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CatalogServiceWebClient {

  private static final String CATALOG_URL = "lb://EVENT-CATALOG-SERVICE";
  private final WebClient.Builder webClientBuilder;

  private WebClient webClient;

  @PostConstruct
  public void init() {
    webClient = webClientBuilder.baseUrl(CATALOG_URL).build();
  }

  @CircuitBreaker(name = "catalog")
  @Retry(name = "catalog")
  public Mono<Boolean> checkMovieIdExists(String movieId) {
    return webClient.get().uri("/api/v1/movies/" + movieId).retrieve()
        .onStatus(HttpStatus.NOT_FOUND::equals,
            clientResponse -> Mono.empty())
        .onStatus((httpStatusCode -> httpStatusCode.value() < 400 && httpStatusCode.value() > 499),
            clientResponse -> Mono.error(new EventApplicationException(
                "Unable to reach Catalog Servers or Something went wrong",
                HttpStatus.INTERNAL_SERVER_ERROR.value())))
        .toBodilessEntity()
        .flatMap(response -> Mono.just(response.getStatusCode().is2xxSuccessful()))
        .switchIfEmpty(Mono.just(false)); // Handle 404 by returning false
  }

  public Mono<Boolean> checkSportsIdExists(String eventId) {
    return Mono.just(false);
  }
}
