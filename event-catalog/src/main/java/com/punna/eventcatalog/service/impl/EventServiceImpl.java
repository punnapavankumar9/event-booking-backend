package com.punna.eventcatalog.service.impl;

import com.punna.eventcatalog.model.Event;
import com.punna.eventcatalog.repository.EventRepository;
import com.punna.eventcatalog.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public Mono<Event> createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Flux<Event> findAllEvents() {
        return eventRepository.findAll();
    }
}
