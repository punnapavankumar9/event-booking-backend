package com.punna.eventcore.service;

import com.punna.eventcore.dto.SeatingLayoutDto;
import com.punna.eventcore.model.SeatLocation;
import java.util.List;
import reactor.core.publisher.Mono;

public interface SeatingLayoutService {

  Mono<SeatingLayoutDto> createSeatingLayout(SeatingLayoutDto seatingLayout);

  Mono<SeatingLayoutDto> getSeatingLayoutById(String seatingLayoutId);

  Mono<SeatingLayoutDto> updateSeatingLayout(SeatingLayoutDto seatingLayout);

  Mono<Void> deleteSeatingLayout(String seatingLayoutId);

  void isSeatingLayoutValid(SeatingLayoutDto seatingLayoutDto);

  Mono<Boolean> areSelectedSeatsValid(String seatingLayoutId, List<SeatLocation> seatLocations);

  Mono<List<String>> getPricingTiers(String id);

  Mono<Integer> getTotalSeats(String id);
}
