package com.punna.eventcatalog.service.impl;

import com.punna.eventcatalog.dto.SeatingLayoutDto;
import com.punna.eventcatalog.mapper.SeatingLayoutMapper;
import com.punna.eventcatalog.model.Event;
import com.punna.eventcatalog.model.Seat;
import com.punna.eventcatalog.model.SeatTier;
import com.punna.eventcatalog.model.SeatingLayout;
import com.punna.eventcatalog.repository.SeatingLayoutRepository;
import com.punna.eventcatalog.service.SeatingLayoutService;
import lombok.RequiredArgsConstructor;
import org.punna.commons.exception.EntityNotFoundException;
import org.punna.commons.exception.EventApplicationException;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SeatingLayoutServiceImpl implements SeatingLayoutService {

    private final SeatingLayoutRepository seatingLayoutRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;


    @Override
    public Mono<SeatingLayoutDto> createSeatingLayout(SeatingLayoutDto seatingLayout) {
        isSeatingLayoutValid(seatingLayout);
        return seatingLayoutRepository
                .save(SeatingLayoutMapper.toSeatingLayout(seatingLayout))
                .map(SeatingLayoutMapper::toSeatingLayoutDto);
    }

    @Override
    public Mono<SeatingLayoutDto> getSeatingLayoutById(String seatingLayoutId) {
        return seatingLayoutRepository
                .findById(seatingLayoutId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(SeatingLayout.class.getSimpleName(),
                        seatingLayoutId)))
                .map(SeatingLayoutMapper::toSeatingLayoutDto);
    }

    @Override
    public Mono<SeatingLayoutDto> updateSeatingLayout(SeatingLayoutDto seatingLayout) {
        String id = seatingLayout.getId();
        if (id == null) {
            throw new EventApplicationException("Id must be provided");
        }
        return seatingLayoutRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(SeatingLayout.class.getSimpleName(), id)))
                .flatMap(seatingLayout1 -> {
                    SeatingLayoutMapper.merge(seatingLayout1, seatingLayout);
                    if (seatingLayout.getSeatTiers() != null) {
                        isSeatingLayoutValid(SeatingLayoutMapper.toSeatingLayoutDto(seatingLayout1));
                    }
                    return seatingLayoutRepository.save(seatingLayout1);
                })
                .map(SeatingLayoutMapper::toSeatingLayoutDto);
    }

    @Override
    public Mono<Void> deleteSeatingLayout(String seatingLayoutId) {
        Query query = new Query(Criteria
                .where("_id")
                .is(seatingLayoutId));
        return reactiveMongoTemplate
                .findAndRemove(query, Event.class)
                .switchIfEmpty(Mono.error(() -> new EntityNotFoundException(Event.class.getSimpleName(),
                        seatingLayoutId)))
                .flatMap((event) -> Mono.empty());
    }

    @Override
    public void isSeatingLayoutValid(SeatingLayoutDto seatingLayoutDto) {
        int capacity = seatingLayoutDto.getCapacity();
        int currentCapacity = 0;
        for (SeatTier tier : seatingLayoutDto.getSeatTiers()) {
            int cnt = isTierValid(tier);
            if (cnt == -1) {
                throw new EventApplicationException("Seating arrangement is not valid", 400);
            }
            currentCapacity += cnt;
        }
        if (currentCapacity != capacity) {
            throw new EventApplicationException("Seating capacity mismatch in seat arrangements", 400);
        }
    }

    public int isTierValid(SeatTier tier) {
        int columns = tier.getColumns();
        int rows = tier.getRows();
        int cnt = 0;
        for (Seat seat : tier.getSeats()) {
            if (seat.getRow() > rows || seat.getColumn() > columns) return -1;
            if (!seat.getIsSpace()) {
                cnt++;
            }
        }
        return cnt;
    }
}
