package com.punna.eventcatalog.service;


import com.punna.eventcatalog.dto.VenueDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VenueService {
    Mono<VenueDto> createVenue(VenueDto venue);

    Mono<Void> deleteVenue(String id);

    Mono<VenueDto> updateVenue(VenueDto venue);

    Flux<VenueDto> findAllVenues(int page);

    Mono<VenueDto> findById(String id);

    Mono<Boolean> exists(String id);
}
