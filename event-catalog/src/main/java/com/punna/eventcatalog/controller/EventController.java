package com.punna.eventcatalog.controller;

import com.punna.eventcatalog.model.Event;
import com.punna.eventcatalog.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Mono<Event>> createEvent(@RequestBody Event event) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.createEvent(event));
    }

    @GetMapping
    public Flux<Event> allEvents() {
        return eventService.findAllEvents();
    }

}
