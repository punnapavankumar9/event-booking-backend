package com.punna.eventcore.service;

import com.punna.eventcore.model.SeatLocation;
import java.math.BigDecimal;
import java.util.List;
import reactor.core.publisher.Mono;

public interface EventSeatStateService {

  Mono<Void> bookSeats(String eventId, List<SeatLocation> seatLocations, BigDecimal amount);

  Mono<Void> blockSeats(String eventId, List<SeatLocation> seatLocations);

  Mono<Void> unblockSeats(String eventId, List<SeatLocation> seatLocations);

  Mono<Void> unBookSeats(String eventId, List<SeatLocation> seatLocations);

  Mono<Boolean> areSelectedSeatsValid(String eventId, List<SeatLocation> seatLocations);

  Mono<Boolean> isOrderValid(String eventId, List<SeatLocation> seatLocations, BigDecimal amount);
}
