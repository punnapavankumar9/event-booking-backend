package com.punna.eventcatalog.controller;

import com.punna.eventcatalog.dto.EventRequestDto;
import com.punna.eventcatalog.dto.EventResponseDto;
import com.punna.eventcatalog.service.EventService;
import lombok.RequiredArgsConstructor;
import org.punna.commons.validation.groups.CreateGroup;
import org.punna.commons.validation.groups.UpdateGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Mono<EventResponseDto>> createEvent(
            @Validated(CreateGroup.class) @RequestBody EventRequestDto event) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.createEvent(event));
    }

    @GetMapping
    public Flux<EventResponseDto> allEvents(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page) {
        return eventService.findAllEvents(page);
    }

    @PatchMapping
    public Mono<EventResponseDto> updateEvent(@Validated(UpdateGroup.class) @RequestBody EventRequestDto event) {
        return eventService.updateEvent(event);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteEvent(@PathVariable String id) {
        return eventService.deleteEvent(id);
    }

    @GetMapping("/{id}")
    public Mono<EventResponseDto> getVenue(@PathVariable String id) {
        return eventService.findById(id);
    }
}
