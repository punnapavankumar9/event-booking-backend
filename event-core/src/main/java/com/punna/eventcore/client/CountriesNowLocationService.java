package com.punna.eventcore.client;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.punna.commons.exception.EventApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


record Country(String name) {

}

record CountryInfo(boolean error, String message, List<Country> data) {

};

record State(String name) {

}

record CountryHighLevelInfoForState(List<State> states) {

}

record StateInfo(boolean error, String message, CountryHighLevelInfoForState data) {

}

record CityInfo(boolean error, String message, List<String> data) {

}


@Service
@RequiredArgsConstructor
public class CountriesNowLocationService implements LocationService {

  private static final String baseUrl = "https://countriesnow.space/api/v0.1";
  private static final String countriesUrl = "/countries/capital";
  private static final String statesUrl = "/countries/states";
  private static final String citiesUrl = "/countries/state/cities";
  private final Builder webClientBuilder;
  private WebClient webClient;

  @PostConstruct
  public void init() {
    webClient = webClientBuilder.baseUrl(baseUrl).build();
  }

  @Override
  public Flux<String> getCountries() {
    return webClient.get().uri(countriesUrl).retrieve()
        .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
            clientResponse -> Mono.error(new EventApplicationException("Unable to get countries")))
        .bodyToMono(CountryInfo.class).flatMapMany(countryInfo -> {

          if (countryInfo.error()) {
            return Flux.error(new EventApplicationException(countryInfo.message()));
          }
          return Flux.fromIterable(countryInfo.data()).map(Country::name);
        });
  }

  @Override
  public Flux<String> getStates(String country) {
    return webClient.get().uri(statesUrl + "/q?country=" + country).retrieve()
        .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
            clientResponse -> Mono.error(new EventApplicationException("Unable to get states")))
        .bodyToMono(StateInfo.class).flatMapMany(stateInfo -> {
          if (stateInfo.error()) {
            return Flux.error(new EventApplicationException(stateInfo.message()));
          }
          return Flux.fromIterable(stateInfo.data().states()).map(State::name);
        });
  }

  @Override
  public Flux<String> getCities(String country, String state) {
    return webClient.get().uri(citiesUrl + String.format("/q?country=%s&state=%s", country, state))
        .retrieve().onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
            clientResponse -> Mono.error(new EventApplicationException("Unable to get states")))
        .bodyToMono(CityInfo.class).flatMapMany(cityInfo -> {
          if (cityInfo.error()) {
            return Flux.error(new EventApplicationException(cityInfo.message()));
          }
          return Flux.fromIterable(cityInfo.data());
        });
  }

  @Override
  public Mono<Boolean> isValidCountry(String country) {
    return this.getCountries().filter(existingCountry -> existingCountry.equalsIgnoreCase(country))
        .hasElements().onErrorReturn(false);
  }

  @Override
  public Mono<Boolean> isValidState(String country, String state) {
    return this.getStates(country).filter(existingState -> existingState.equalsIgnoreCase(state))
        .hasElements().onErrorReturn(false);
  }

  @Override
  public Mono<Boolean> isValidCity(String country, String state, String city) {
    return this.getCities(country, state)
        .filter(existingCity -> existingCity.equalsIgnoreCase(city)).hasElements()
        .onErrorReturn(false);
  }
}
