package com.punna.eventcatalog.service;

import com.punna.eventcatalog.dto.SeatingLayoutDto;
import com.punna.eventcatalog.model.SeatLocation;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SeatingLayoutService {

    Mono<SeatingLayoutDto> createSeatingLayout(SeatingLayoutDto seatingLayout);

    Mono<SeatingLayoutDto> getSeatingLayoutById(String seatingLayoutId);

    Mono<SeatingLayoutDto> updateSeatingLayout(SeatingLayoutDto seatingLayout);

    Mono<Void> deleteSeatingLayout(String seatingLayoutId);

    void isSeatingLayoutValid(SeatingLayoutDto seatingLayoutDto);

    Mono<Boolean> areSelectedSeatsValid(String seatingLayoutId, List<SeatLocation> seatLocations);
}
