package com.punna.eventcore.service;

import com.punna.eventcore.dto.EventRequestDto;
import com.punna.eventcore.dto.EventResponseDto;
import com.punna.eventcore.model.Event;
import com.punna.eventcore.model.EventType;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventService {

  Mono<EventResponseDto> createEvent(EventRequestDto event);

  Mono<List<EventResponseDto>> createEvents(Flux<EventRequestDto> eventRequestDtos);

  Flux<EventResponseDto> findAllEvents(Integer page);

  Mono<EventResponseDto> updateEvent(EventRequestDto event);

  Mono<Void> deleteEvent(String id);

  Mono<EventResponseDto> findById(String id);

  Mono<Boolean> isAdminOrOwner(String id);

  Mono<Boolean> isAdminOrOwner(Event event);

  Flux<EventResponseDto> getEventsByType(EventType eventType);

}
