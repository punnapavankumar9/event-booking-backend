package com.punna.eventcore.service.impl;

import com.punna.eventcore.dto.EventRequestDto;
import com.punna.eventcore.dto.EventResponseDto;
import com.punna.eventcore.mapper.EventMapper;
import com.punna.eventcore.model.Event;
import com.punna.eventcore.model.EventType;
import com.punna.eventcore.model.Venue;
import com.punna.eventcore.repository.EventRepository;
import com.punna.eventcore.service.AuthService;
import com.punna.eventcore.service.EventService;
import com.punna.eventcore.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.punna.commons.exception.EntityNotFoundException;
import org.punna.commons.exception.EventApplicationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ReactiveMongoTemplate mongoTemplate;
    private final VenueService venueService;
    private final AuthService authService;

    @Override
    public Mono<EventResponseDto> createEvent(EventRequestDto eventRequestDto) {
        return venueService
                .getSeatingLayoutId(eventRequestDto.getVenueId())
                .flatMap(seatingLayoutId -> {
                    eventRequestDto.setSeatingLayoutId(seatingLayoutId);
                    return eventRepository
                            .save(EventMapper.toEvent(eventRequestDto))
                            .map(EventMapper::toEventResponseDto);
                });
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
                    return isAdminOrOwner(event)
                            .filter(m -> m)
                            .flatMap(permission -> {
                                if (eventRequestDto.getVenueId() != null) {
                                    return venueService
                                            .getSeatingLayoutId(eventRequestDto.getVenueId())
                                            .flatMap(seatingLayoutId -> {
                                                event.setSeatingLayoutId(seatingLayoutId);
                                                return eventRepository.save(event);
                                            })
                                            .switchIfEmpty(Mono.error(new EntityNotFoundException(Venue.class.getSimpleName(),
                                                    eventRequestDto.getVenueId()
                                            )));
                                }
                                return eventRepository.save(event);
                            })
                            .switchIfEmpty(Mono.error(new AccessDeniedException(
                                    "You don't have permission to edit this Event")));
                })
                .map(EventMapper::toEventResponseDto);
    }

    @Override
    @PreAuthorize("@eventServiceImpl.isAdminOrOwner(#id)")
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

    @Override
    public Mono<Boolean> isAdminOrOwner(String id) {
        return authService
                .hasRole("ADMIN")
                .filter(m -> m)
                .switchIfEmpty(this
                        .findById(id)
                        .flatMap(event -> authService
                                .getUserName()
                                .map(username -> username.equals(event.getOrganizerId()))));
    }

    @Override
    public Mono<Boolean> isAdminOrOwner(Event event) {
        return authService
                .hasRole("ADMIN")
                .filter(m -> m)
                .switchIfEmpty(authService
                        .getUserName()
                        .map(username -> username.equals(event.getOrganizerId())));
    }

    @Override
    public Flux<EventResponseDto> getEventsByType(EventType eventType) {
        return eventRepository.findAllByEventType(eventType)
                .map(EventMapper::toEventResponseDto);
    }
}
