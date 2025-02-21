package com.punna.eventcore.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LocationService {

  Flux<String> getCountries();

  Flux<String> getStates(String country);

  Flux<String> getCities(String country, String state);

  Mono<Boolean> isValidCountry(String country);

  Mono<Boolean> isValidState(String country, String state);

  Mono<Boolean> isValidCity(String country, String state, String city);
}
