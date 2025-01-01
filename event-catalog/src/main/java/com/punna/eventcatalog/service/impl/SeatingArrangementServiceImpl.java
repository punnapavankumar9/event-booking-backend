package com.punna.eventcatalog.service.impl;

import com.punna.eventcatalog.dto.SeatingArrangementDto;
import com.punna.eventcatalog.mapper.SeatingArrangementMapper;
import com.punna.eventcatalog.model.Event;
import com.punna.eventcatalog.model.SeatingArrangement;
import com.punna.eventcatalog.repository.SeatingArrangementRepository;
import com.punna.eventcatalog.service.SeatingArrangementService;
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
public class SeatingArrangementServiceImpl implements SeatingArrangementService {

    private final SeatingArrangementRepository seatingArrangementRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;


    @Override
    public Mono<SeatingArrangementDto> createSeatingArrangement(SeatingArrangementDto seatingArrangement) {
        return seatingArrangementRepository
                .save(SeatingArrangementMapper.toSeatingArrangement(seatingArrangement))
                .map(SeatingArrangementMapper::toSeatingArrangementDto);
    }

    @Override
    public Mono<SeatingArrangementDto> getSeatingArrangementById(String seatingArrangementId) {
        return seatingArrangementRepository
                .findById(seatingArrangementId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(SeatingArrangement.class.getSimpleName(),
                        seatingArrangementId)))
                .map(SeatingArrangementMapper::toSeatingArrangementDto);
    }

    @Override
    public Mono<SeatingArrangementDto> updateSeatingArrangement(SeatingArrangementDto seatingArrangement) {
        String id = seatingArrangement.getId();
        if (id == null) {
            throw new EventApplicationException("Id must be provided");
        }
        return seatingArrangementRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(SeatingArrangement.class.getSimpleName(), id)))
                .flatMap(seatingArrangement1 -> {
                    SeatingArrangementMapper.merge(seatingArrangement1, seatingArrangement);
                    return seatingArrangementRepository.save(seatingArrangement1);
                })
                .map(SeatingArrangementMapper::toSeatingArrangementDto);
    }

    @Override
    public Mono<Void> deleteSeatingArrangement(String seatingArrangementId) {
        Query query = new Query(Criteria
                .where("_id")
                .is(seatingArrangementId));
        return reactiveMongoTemplate
                .findAndRemove(query, Event.class)
                .switchIfEmpty(Mono.error(() -> new EntityNotFoundException(Event.class.getSimpleName(),
                        seatingArrangementId)))
                .flatMap((event) -> Mono.empty());
    }
}
