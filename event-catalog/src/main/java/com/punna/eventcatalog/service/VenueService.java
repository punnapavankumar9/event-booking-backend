package com.punna.eventcatalog.service;


import com.punna.eventcatalog.model.Venue;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VenueService {
    Mono<ObjectId> createVenue(Venue venue);

    Mono<Void> deleteVenue(ObjectId id);

    Mono<Void> updateVenue(ObjectId id, Venue venue);

    Flux<Venue> getAllVenues();
}
