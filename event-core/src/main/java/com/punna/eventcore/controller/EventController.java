package com.punna.eventcore.controller;

import com.punna.eventcore.dto.EventRequestDto;
import com.punna.eventcore.dto.EventResponseDto;
import com.punna.eventcore.model.EventType;
import com.punna.eventcore.service.EventService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.punna.commons.exception.EventApplicationException;
import org.punna.commons.validation.groups.CreateGroup;
import org.punna.commons.validation.groups.UpdateGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(event));
  }


  @PostMapping(value = "", params = {"batch"})
  public ResponseEntity<Mono<List<EventResponseDto>>> createEvents(
      @Validated({CreateGroup.class}) @RequestBody Flux<EventRequestDto> events,
      @RequestParam() Boolean batch) {
    if (batch) {
      return ResponseEntity.status(HttpStatus.CREATED).body(eventService
          .createEvents(events));
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Mono.error(
        new EventApplicationException("Something Went Wrong", HttpStatus.FORBIDDEN.value())));
  }

  @GetMapping
  public Flux<EventResponseDto> allEvents(
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page) {
    return eventService.findAllEvents(page);
  }

  @PatchMapping
  public Mono<EventResponseDto> updateEvent(
      @Validated(UpdateGroup.class) @RequestBody EventRequestDto event) {
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

  @GetMapping("/byEventType/{}")
  public Flux<EventResponseDto> getVenuesByType(@PathVariable EventType eventType) {
    return eventService.getEventsByType(eventType);
  }

}
