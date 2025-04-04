package com.punna.eventcore.controller;

import com.punna.commons.exception.EventApplicationException;
import com.punna.commons.validation.groups.CreateGroup;
import com.punna.commons.validation.groups.UpdateGroup;
import com.punna.eventcore.dto.BookingPageInfo;
import com.punna.eventcore.dto.EventRequestDto;
import com.punna.eventcore.dto.EventResponseDto;
import com.punna.eventcore.dto.EventsForVenueProjection;
import com.punna.eventcore.dto.EventInfo;
import com.punna.eventcore.dto.ShowListingDto;
import com.punna.eventcore.dto.projections.EventNameAndIdProjection;
import com.punna.eventcore.model.EventType;
import com.punna.eventcore.service.EventService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

  @PostMapping(value = "", params = {"bulk"})
  public ResponseEntity<Mono<List<EventResponseDto>>> createEvents(
      @Validated({CreateGroup.class}) @RequestBody Flux<EventRequestDto> events,
      @RequestParam() Boolean bulk) {
    if (bulk) {
      return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvents(events));
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
  public Mono<EventResponseDto> getEvent(@PathVariable String id) {
    return eventService.findById(id);
  }

  @GetMapping("/booking-info/{id}")
  public Mono<BookingPageInfo> getBookingPageDetailsFor(@PathVariable String id) {
    return eventService.getBookingPageDetailsFor(id);
  }

  @GetMapping("/byEventType/{type}")
  public Flux<EventResponseDto> getEventsByType(@PathVariable EventType type) {
    return eventService.getEventsByType(type);
  }

  @GetMapping("/event-dates/byEventId")
  public Flux<Instant> getEventDatesByEventId(@RequestParam() String eventId,
      @RequestParam() Instant from) {
    return eventService.getAllStartDatesByEventId(eventId, from);
  }

  @GetMapping("/byEventId/{eventId}")
  public Flux<ShowListingDto> getShowListingsWithEventIdForCityStartTimeBetween(
      @PathVariable String eventId, @RequestParam String city, @RequestParam() Instant startTime,
      @RequestParam Instant endTime) {
    log.info("Getting events for city : {}, eventID: {}", city, eventId);
    return eventService.getShowListings(eventId, startTime, endTime, city);
  }

  @GetMapping("/byEventId/{eventId}/{venueId}")
  public Flux<EventsForVenueProjection> getEventsForEventId(@PathVariable String eventId,
      @PathVariable String venueId) {
    return eventService.getEventsByEventId(eventId, venueId);
  }


  @PostMapping("/event-names")
  public Flux<EventNameAndIdProjection> getEventNames(@RequestBody List<String> eventIds) {
    return eventService.getEventNamesForIds(eventIds);
  }

  // Give full order info along with venueName, venueLocation details.
  @GetMapping("/event-info/{eventId}")
  public Mono<EventInfo> getEventInfo(@PathVariable String eventId){
    return eventService.getEventInfo(eventId);
  }
}
