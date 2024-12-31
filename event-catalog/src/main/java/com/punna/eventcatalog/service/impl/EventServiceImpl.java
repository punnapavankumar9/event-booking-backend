package com.punna.eventcatalog.service.impl;

import com.punna.eventcatalog.dto.EventRequestDto;
import com.punna.eventcatalog.dto.EventResponseDto;
import com.punna.eventcatalog.mapper.EventMapper;
import com.punna.eventcatalog.model.Event;
import com.punna.eventcatalog.repository.EventRepository;
import com.punna.eventcatalog.service.EventService;
import lombok.RequiredArgsConstructor;
import org.punna.commons.exception.EntityNotFoundException;
import org.punna.commons.exception.EventApplicationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<EventResponseDto> createEvent(EventRequestDto eventRequestDto) {
        return eventRepository
                .save(EventMapper.toEvent(eventRequestDto))
                .map(EventMapper::toEventResponseDto);
    }

    @Override
    public Flux<EventResponseDto> findAllEvents(Integer page) {
        Pageable pageRequest = PageRequest
                .of(page, 10)
                .withSort(Sort
                        .by("createdAt")
                        .descending());
        return eventRepository
                .findAllBy(pageRequest)
                .map(EventMapper::toEventResponseDto);
    }

    @Override
    public Mono<EventResponseDto> updateEvent(EventRequestDto eventRequestDto) {
        String id = eventRequestDto.getId();
        if (id == null) {
            throw new EventApplicationException("event object must contain id to update record");
        }
        return eventRepository
                .findById(eventRequestDto.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Event", id)))
                .flatMap(event -> {
                    EventMapper.merge(event, eventRequestDto);
                    return eventRepository.save(event);
                })
                .map(EventMapper::toEventResponseDto);
    }

    @Override
    public Mono<Void> deleteEvent(String id) {
        Query query = new Query(Criteria
                .where("_id")
                .is(id));
        return mongoTemplate
                .findAndRemove(query, Event.class)
                .switchIfEmpty(Mono.error(() -> new EntityNotFoundException(Event.class.getSimpleName(), id)))
                .flatMap((event) -> Mono.empty());
    }

    @Override
    public Mono<EventResponseDto> findById(String id) {
        return eventRepository
                .findById(id)
                .switchIfEmpty(Mono.error(() -> new EntityNotFoundException(Event.class.getSimpleName(), id)))
                .map(EventMapper::toEventResponseDto);
    }
}
