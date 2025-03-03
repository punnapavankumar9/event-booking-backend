package com.punna.eventcore.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.punna.commons.exception.EntityNotFoundException;
import org.punna.commons.exception.EventApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

  public Mono<Boolean> checkMovieIdExists(String movieId) {
    return webClient.get().uri("/api/v1/movies/" + movieId).retrieve()
        .onStatus(HttpStatusCode::is4xxClientError,
            clientResponse -> Mono.just(new EntityNotFoundException("Movie", movieId)))
        .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.just(
            new EventApplicationException("Unable to reach Catalog Servers",
                HttpStatus.INTERNAL_SERVER_ERROR.value()))).toBodilessEntity()
        .map(response -> response.getStatusCode().is2xxSuccessful());
  }

  public Mono<Boolean> checkSportsIdExists(String eventId) {
    return Mono.just(false);
  }
}
