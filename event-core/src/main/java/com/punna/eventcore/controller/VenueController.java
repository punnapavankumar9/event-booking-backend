package com.punna.eventcore.controller;

import com.punna.eventcore.dto.VenueDto;
import com.punna.eventcore.dto.projections.VenueNameWithLayoutIdProjection;
import com.punna.eventcore.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.punna.commons.validation.groups.CreateGroup;
import org.punna.commons.validation.groups.UpdateGroup;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/venues")
@RestController
@RequiredArgsConstructor
@Validated
public class VenueController {

  private final VenueService venueService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<VenueDto> createVenue(@Validated(CreateGroup.class) @RequestBody VenueDto venue) {
    return venueService.createVenue(venue);
  }

  @GetMapping(value = "/search")
  public Flux<VenueNameWithLayoutIdProjection> searchVenue(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "page", required = false, defaultValue = "0") int page) {
    return venueService.findByName(name, page);
  }

  @GetMapping
  public Flux<VenueDto> allEvents(
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page) {
    return venueService.findAllVenues(page);
  }

  @PatchMapping
  public Mono<VenueDto> updateVenue(@Validated(UpdateGroup.class) @RequestBody VenueDto venue) {
    return venueService.updateVenue(venue);
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteVenue(@PathVariable String id) {
    return venueService.deleteVenue(id);
  }

  @GetMapping("/{id}")
  public Mono<VenueDto> getVenue(@PathVariable String id) {
    return venueService.findById(id);
  }
}
