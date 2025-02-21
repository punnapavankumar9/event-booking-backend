package com.punna.eventcore.service.impl;

import com.punna.eventcore.dto.VenueDto;
import com.punna.eventcore.mapper.VenueMapper;
import com.punna.eventcore.model.Venue;
import com.punna.eventcore.repository.VenueRepository;
import com.punna.eventcore.service.AuthService;
import com.punna.eventcore.service.SeatingLayoutService;
import com.punna.eventcore.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.punna.commons.exception.EntityNotFoundException;
import org.punna.commons.exception.EventApplicationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

  private final VenueRepository venueRepository;
  private final SeatingLayoutService seatingLayoutService;
  private final ReactiveMongoTemplate mongoTemplate;
  private final AuthService authService;

  @Override
  public Mono<VenueDto> createVenue(VenueDto venue) {
    return seatingLayoutService
        .getSeatingLayoutById(venue.getSeatingLayoutId())
        .flatMap(seatArr -> authService.getUserName().map(username -> {
          venue.setOwnerId(username);
          return venue;
        }))
        .flatMap(venueDto -> venueRepository
            .save(VenueMapper.toVenue(venueDto))
            .map(VenueMapper::toVenueDto));
  }

  @Override
  @PreAuthorize("@venueServiceImpl.isAdminOrOwner(#id)")
  public Mono<Void> deleteVenue(String id) {
    Query query = new Query(Criteria
        .where("_id")
        .is(id));
    return mongoTemplate
        .findAndRemove(query, Venue.class)
        .switchIfEmpty(
            Mono.error(() -> new EntityNotFoundException(Venue.class.getSimpleName(), id)))
        .flatMap((e) -> Mono.empty());
  }

  @Override
  public Mono<VenueDto> updateVenue(VenueDto venue) {
    String id = venue.getId();
    if (id == null) {
      throw new EventApplicationException("Venue object must contain id to update record");
    }
    return venueRepository
        .findById(id)
        .switchIfEmpty(
            Mono.error(() -> new EntityNotFoundException(Venue.class.getSimpleName(), id)))
        .flatMap(existingVenue -> {
          VenueMapper.merge(venue, existingVenue);
          return isAdminOrOwner(existingVenue)
              .filter(m -> m)
              .flatMap(permission -> {
                if (venue.getSeatingLayoutId() != null) {
                  return seatingLayoutService
                      .getSeatingLayoutById(venue.getSeatingLayoutId())
                      .flatMap(seatingLayoutDto -> venueRepository.save(existingVenue));
                }
                return venueRepository.save(existingVenue);
              })
              .switchIfEmpty(Mono.error(new AccessDeniedException(
                  "You don't have permission to edit this Venue")));
        })
        .map(VenueMapper::toVenueDto);
  }

  @Override
  public Flux<VenueDto> findAllVenues(int page) {
    Pageable pageRequest = PageRequest
        .of(page, 10)
        .withSort(Sort
            .by("createdAt")
            .descending());
    return venueRepository
        .findAllBy(pageRequest)
        .map(VenueMapper::toVenueDto);
  }

  @Override
  public Mono<VenueDto> findById(String id) {
    return venueRepository
        .findById(id)
        .switchIfEmpty(
            Mono.error(() -> new EntityNotFoundException(Venue.class.getSimpleName(), id)))
        .map(VenueMapper::toVenueDto);
  }

  @Override
  public Mono<Boolean> exists(String id) {
    return venueRepository.existsById(id);
  }

  @Override
  public Mono<Boolean> isAdminOrOwner(String id) {
    return authService
        .hasRole("ADMIN")
        .filter(m -> m)
        .switchIfEmpty(this
            .findById(id)
            .flatMap(v -> authService
                .getUserName()
                .map(s -> s.equals(v.getOwnerId()))));
  }

  @Override
  public Mono<Boolean> isAdminOrOwner(Venue venue) {
    return authService
        .hasRole("ADMIN")
        .filter(m -> m)
        .switchIfEmpty(authService
            .getUserName()
            .map(username -> username.equals(venue.getOwnerId())));
  }

  @Override
  public Mono<String> getSeatingLayoutId(String id) {

    Query query = Query.query(Criteria
        .where("_id")
        .is(id));
    query
        .fields()
        .include("seatingLayoutId")
        .exclude("_id");

    return mongoTemplate
        .findOne(query, Venue.class)
        .map(Venue::getSeatingLayoutId)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(Venue.class.getSimpleName(), id)));
  }
}
