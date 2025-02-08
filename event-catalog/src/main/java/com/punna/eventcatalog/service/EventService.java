package com.punna.eventcatalog.service;

import com.punna.eventcatalog.dto.EventRequestDto;
import com.punna.eventcatalog.dto.EventResponseDto;
import com.punna.eventcatalog.model.Event;
import com.punna.eventcatalog.model.EventType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventService {

    Mono<EventResponseDto> createEvent(EventRequestDto event);

    Flux<EventResponseDto> findAllEvents(Integer page);

    Mono<EventResponseDto> updateEvent(EventRequestDto event);

    Mono<Void> deleteEvent(String id);

    Mono<EventResponseDto> findById(String id);

    Mono<Boolean> isAdminOrOwner(String id);

    Mono<Boolean> isAdminOrOwner(Event event);

    Flux<EventResponseDto> getEventsByType(EventType eventType);
}
