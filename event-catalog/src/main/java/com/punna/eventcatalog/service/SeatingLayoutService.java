package com.punna.eventcatalog.service;

import com.punna.eventcatalog.dto.SeatingLayoutDto;
import reactor.core.publisher.Mono;

public interface SeatingLayoutService {

    Mono<SeatingLayoutDto> createSeatingLayout(SeatingLayoutDto seatingLayout);

    Mono<SeatingLayoutDto> getSeatingLayoutById(String seatingLayoutId);

    Mono<SeatingLayoutDto> updateSeatingLayout(SeatingLayoutDto seatingLayout);

    Mono<Void> deleteSeatingLayout(String seatingLayoutId);

    void isSeatingLayoutValid(SeatingLayoutDto seatingLayoutDto);

}
