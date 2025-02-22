package com.punna.eventcore.service.impl;

import com.punna.eventcore.client.CatalogServiceWebClient;
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
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.punna.commons.exception.EntityNotFoundException;
import org.punna.commons.exception.EventApplicationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

  private final EventRepository eventRepository;
  private final ReactiveMongoTemplate mongoTemplate;
  private final VenueService venueService;
  private final AuthService authService;

  private final CatalogServiceWebClient catalogServiceWebClient;

  @Override
  public Mono<EventResponseDto> createEvent(EventRequestDto eventRequestDto) {
    if (eventRequestDto.getEventDurationDetails().getStartTime()
        .isAfter(eventRequestDto.getEventDurationDetails().getEndTime())) {
      return Mono.error(new EventApplicationException("Start time cannot be after end time"));
    }
    Mono<Boolean> idCheckMono = switch (eventRequestDto.getEventType()) {
      case MOVIE -> catalogServiceWebClient.checkMovieIdExists(eventRequestDto.getEventId());
      case SPORTS -> catalogServiceWebClient.checkSportsIdExists(eventRequestDto.getEventId());
      default -> Mono.error(new EventApplicationException("Invalid event category"));
    };
    return Mono.zip(venueService.getSeatingLayoutId(eventRequestDto.getVenueId()), idCheckMono)
        .flatMap(tuple -> this.checkForOverlaps(eventRequestDto).flatMap((overlap) -> {
          String seatingLayoutId = tuple.getT1();
          Boolean eventIdExists = tuple.getT2();
          if (!eventIdExists) {
            return Mono.error(new EventApplicationException("Event ID does not exist",
                HttpStatus.BAD_REQUEST.value()));
          }
          if (!overlap) {
            eventRequestDto.setSeatingLayoutId(seatingLayoutId);
            return eventRepository.save(EventMapper.toEvent(eventRequestDto))
                .map(EventMapper::toEventResponseDto);
          }
          return Mono.error(new EventApplicationException(
              "Event Overlap found. please check for existing events."));
        }));
  }

  @Override
  @Transactional
  public Mono<List<EventResponseDto>> createEvents(Flux<EventRequestDto> eventRequestDtos) {
    return eventRequestDtos.concatMap(event -> createEvent(event).onErrorMap(error -> {
      log.error("Error in event creation: {}", error.getMessage());
      return new EventApplicationException("Error while creating events: " + error.getMessage(),
          HttpStatus.BAD_REQUEST.value());
    }).doOnError(error -> {
      // Log additional details if needed
      System.err.println("Transaction failed and rolled back: " + error.getMessage());
    })).collectList().onErrorResume(e -> Mono.error(
        new EventApplicationException(e.getMessage(), HttpStatus.BAD_REQUEST.value())));
  }


  @Override
  public Flux<EventResponseDto> findAllEvents(Integer page) {
    Pageable pageRequest = PageRequest.of(page, 10).withSort(Sort.by("createdAt").descending());
    return eventRepository.findAllBy(pageRequest).map(EventMapper::toEventResponseDto);
  }

  @Override
  public Mono<EventResponseDto> updateEvent(EventRequestDto eventRequestDto) {
    String id = eventRequestDto.getId();
    if (id == null) {
      throw new EventApplicationException("event object must contain id to update record");
    }
    return eventRepository.findById(eventRequestDto.getId())
        .switchIfEmpty(Mono.error(new EntityNotFoundException("Event", id))).flatMap(event -> {
          EventMapper.merge(event, eventRequestDto);
          return isAdminOrOwner(event).filter(m -> m).flatMap(permission -> {
            if (eventRequestDto.getVenueId() != null) {
              return venueService.getSeatingLayoutId(eventRequestDto.getVenueId())
                  .flatMap(seatingLayoutId -> {
                    event.setSeatingLayoutId(seatingLayoutId);
                    return eventRepository.save(event);
                  }).switchIfEmpty(Mono.error(
                      new EntityNotFoundException(Venue.class.getSimpleName(),
                          eventRequestDto.getVenueId())));
            }
            return eventRepository.save(event);
          }).switchIfEmpty(Mono.error(
              new AccessDeniedException("You don't have permission to edit this Event")));
        }).map(EventMapper::toEventResponseDto);
  }

  @Override
  @PreAuthorize("@eventServiceImpl.isAdminOrOwner(#id)")
  public Mono<Void> deleteEvent(String id) {
    Query query = new Query(Criteria.where("_id").is(id));
    return mongoTemplate.findAndRemove(query, Event.class).switchIfEmpty(
            Mono.error(() -> new EntityNotFoundException(Event.class.getSimpleName(), id)))
        .flatMap((event) -> Mono.empty());
  }

  @Override
  public Mono<EventResponseDto> findById(String id) {
    return eventRepository.findById(id).switchIfEmpty(
            Mono.error(() -> new EntityNotFoundException(Event.class.getSimpleName(), id)))
        .map(EventMapper::toEventResponseDto);
  }

  @Override
  public Mono<Boolean> isAdminOrOwner(String id) {
    return authService.hasRole("ADMIN").filter(m -> m).switchIfEmpty(this.findById(id).flatMap(
        event -> authService.getUserName()
            .map(username -> username.equals(event.getOrganizerId()))));
  }

  @Override
  public Mono<Boolean> isAdminOrOwner(Event event) {
    return authService.hasRole("ADMIN").filter(m -> m).switchIfEmpty(
        authService.getUserName().map(username -> username.equals(event.getOrganizerId())));
  }

  @Override
  public Flux<EventResponseDto> getEventsByType(EventType eventType) {
    return eventRepository.findAllByEventType(eventType).map(EventMapper::toEventResponseDto);
  }

  @Override
  public Flux<EventResponseDto> getEventsByEventId(String eventId) {
    return this.eventRepository.findAllByEventId(eventId, Sort.by("createdAt").descending());
  }

  public Mono<Boolean> checkForOverlaps(EventRequestDto eventRequestDto) {
    Instant newStartTime = eventRequestDto.getEventDurationDetails().getStartTime();
    Instant newEndTime = eventRequestDto.getEventDurationDetails().getEndTime();
    String newVenueId = eventRequestDto.getVenueId(); // Assume venueId is a field in Event

    if (newStartTime == null || newEndTime == null || newVenueId == null) {
      return Mono.error(
          new IllegalArgumentException("Start time, end time, and venue ID cannot be null"));
    }
    // Build query to find overlapping events at the same venue
    Query query = Query.query(
        Criteria.where("venueId").is(newVenueId) // Only check events with matching venueId
            .andOperator( // Combine with time overlap conditions
                new Criteria().orOperator(
                    // Case 1: New event starts during an existing event
                    Criteria.where("eventDurationDetails.startTime").lte(newStartTime)
                        .and("eventDurationDetails.endTime").gte(newStartTime),
                    // Case 2: New event ends during an existing event
                    Criteria.where("eventDurationDetails.startTime").lte(newEndTime)
                        .and("eventDurationDetails.endTime").gte(newEndTime),
                    // Case 3: New event encompasses an existing event
                    Criteria.where("eventDurationDetails.startTime").gte(newStartTime)
                        .and("eventDurationDetails.endTime").lte(newEndTime),
                    // Case 4: Existing event encompasses new event
                    Criteria.where("eventDurationDetails.startTime").lte(newStartTime)
                        .and("eventDurationDetails.endTime").gte(newEndTime))));

    // Exclude the new event itself if it’s an update (optional)
    if (eventRequestDto.getId() != null) {
      query.addCriteria(Criteria.where("id").ne(eventRequestDto.getId()));
    }

    return mongoTemplate.exists(query, Event.class)
        .defaultIfEmpty(false); // Returns false if no overlaps found
  }
}
