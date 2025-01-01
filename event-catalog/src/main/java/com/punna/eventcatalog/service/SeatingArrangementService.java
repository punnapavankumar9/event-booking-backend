package com.punna.eventcatalog.service;

import com.punna.eventcatalog.dto.SeatingArrangementDto;
import reactor.core.publisher.Mono;

public interface SeatingArrangementService {

    Mono<SeatingArrangementDto> createSeatingArrangement(SeatingArrangementDto seatingArrangement);

    Mono<SeatingArrangementDto> getSeatingArrangementById(String seatingArrangementId);

    Mono<SeatingArrangementDto> updateSeatingArrangement(SeatingArrangementDto seatingArrangement);

    Mono<Void> deleteSeatingArrangement(String seatingArrangementId);

}
