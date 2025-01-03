package com.punna.eventcatalog.service;

import com.punna.eventcatalog.model.SeatLocation;
import reactor.core.publisher.Mono;

import java.util.List;

public interface EventSeatStateService {


    Mono<Void> bookSeats(String eventId, List<SeatLocation> seatLocations);

    Mono<Void> blockSeats(String eventId, List<SeatLocation> seatLocations);

    Mono<Void> unblockSeats(String eventId, List<SeatLocation> seatLocations);

    Mono<Void> unBookSeats(String eventId, List<SeatLocation> seatLocations);

    Mono<Boolean> areSelectedSeatsValid(String eventId, List<SeatLocation> seatLocations);
}
