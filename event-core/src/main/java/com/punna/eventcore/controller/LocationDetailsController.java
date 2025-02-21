package com.punna.eventcore.controller;

import com.punna.eventcore.client.LocationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class LocationDetailsController {

  private final LocationService locationService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/countries")
  public Mono<List<String>> getCountries() {
    return locationService.getCountries().collectList();
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/states", params = {"country"})
  public Mono<List<String>> getStates(@RequestParam("country") String country) {
    return locationService.getStates(country).collectList();
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/cities", params = {"country",
      "state"})
  public Mono<List<String>> getCities(@RequestParam("country") String country,
      @RequestParam("state") String state) {
    return locationService.getCities(country, state).collectList();
  }
}
