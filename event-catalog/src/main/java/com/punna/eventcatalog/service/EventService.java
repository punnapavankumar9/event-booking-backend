package com.punna.eventcatalog.service;

import com.punna.eventcatalog.model.Event;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventService {

    Mono<Event> createEvent(Event event);

    Flux<Event> findAllEvents();

}
