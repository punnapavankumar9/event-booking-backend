package com.punna.eventcatalog.service;

import com.punna.eventcatalog.dto.EventRequestDto;
import com.punna.eventcatalog.dto.EventResponseDto;
import com.punna.eventcatalog.model.Event;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventService {

    Mono<EventResponseDto> createEvent(EventRequestDto event);

    Flux<EventResponseDto> findAllEvents(Integer page);

    Mono<EventResponseDto> updateEvent(EventRequestDto event);
}
