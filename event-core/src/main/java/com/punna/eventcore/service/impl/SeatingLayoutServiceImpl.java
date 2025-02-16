package com.punna.eventcore.service.impl;

import com.punna.eventcore.dto.SeatingLayoutDto;
import com.punna.eventcore.mapper.SeatingLayoutMapper;
import com.punna.eventcore.model.Event;
import com.punna.eventcore.model.Seat;
import com.punna.eventcore.model.SeatLocation;
import com.punna.eventcore.model.SeatingLayout;
import com.punna.eventcore.repository.SeatingLayoutRepository;
import com.punna.eventcore.service.SeatingLayoutService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.punna.commons.exception.EntityNotFoundException;
import org.punna.commons.exception.EventApplicationException;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SeatingLayoutServiceImpl implements SeatingLayoutService {

  private final SeatingLayoutRepository seatingLayoutRepository;
  private final ReactiveMongoTemplate reactiveMongoTemplate;


  @Override
  public Mono<SeatingLayoutDto> createSeatingLayout(SeatingLayoutDto seatingLayout) {
    isSeatingLayoutValid(seatingLayout);
    return isLayoutNameUnique(seatingLayout.getName())
        .flatMap(isUnique -> {
              if (isUnique) {
                return seatingLayoutRepository
                    .save(SeatingLayoutMapper.toSeatingLayout(seatingLayout))
                    .map(SeatingLayoutMapper::toSeatingLayoutDto);
              } else {
                return Mono.error(
                    new EventApplicationException("Seating layout with this name already exists"));
              }
            }
        );
  }

  @Override
  public Mono<SeatingLayoutDto> getSeatingLayoutById(String seatingLayoutId) {
    return seatingLayoutRepository
        .findById(seatingLayoutId)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(SeatingLayout.class.getSimpleName(),
            seatingLayoutId
        )))
        .map(SeatingLayoutMapper::toSeatingLayoutDto);
  }

  @Override
  public Mono<SeatingLayoutDto> updateSeatingLayout(SeatingLayoutDto seatingLayout) {
    String id = seatingLayout.getId();
    if (id == null) {
      throw new EventApplicationException("Id must be provided");
    }
    return seatingLayoutRepository
        .findById(id)
        .switchIfEmpty(
            Mono.error(new EntityNotFoundException(SeatingLayout.class.getSimpleName(), id)))
        .flatMap(seatingLayout1 -> {
          SeatingLayoutMapper.merge(seatingLayout1, seatingLayout);
          if (seatingLayout.getSeats() != null) {
            isSeatingLayoutValid(SeatingLayoutMapper.toSeatingLayoutDto(seatingLayout1));
          }
          return seatingLayoutRepository.save(seatingLayout1);
        })
        .map(SeatingLayoutMapper::toSeatingLayoutDto);
  }

  @Override
  public Mono<Void> deleteSeatingLayout(String seatingLayoutId) {
    Query query = new Query(Criteria
        .where("_id")
        .is(seatingLayoutId));
    return reactiveMongoTemplate
        .findAndRemove(query, Event.class)
        .switchIfEmpty(Mono.error(() -> new EntityNotFoundException(Event.class.getSimpleName(),
            seatingLayoutId
        )))
        .flatMap((event) -> Mono.empty());
  }

  @Override
  public void isSeatingLayoutValid(SeatingLayoutDto seatingLayoutDto) {
    int capacity = seatingLayoutDto.getCapacity();
    int currentCapacity = 0;
    int rows = seatingLayoutDto.getRows();
    int columns = seatingLayoutDto.getColumns();
    for (Seat seat : seatingLayoutDto.getSeats()) {
      int cnt = isSeatValid(rows, columns, seat);
      if (cnt == -1) {
        throw new EventApplicationException("Seating arrangement is not valid", 400);
      }
      currentCapacity += cnt;
    }
    if (currentCapacity != capacity) {
      throw new EventApplicationException("Seating capacity mismatch in seat arrangements", 400);
    }
  }

  public Mono<Boolean> isLayoutNameUnique(String name) {
    return seatingLayoutRepository.findByName(name).count().map(count -> count == 0);
  }

  @Override
  public Mono<Boolean> areSelectedSeatsValid(String seatingLayoutId,
      List<SeatLocation> seatLocations) {
    return seatingLayoutRepository
        .validatedSelectedSeatsAreValid(seatingLayoutId, seatLocations)
        .switchIfEmpty(Mono.error(new EventApplicationException(
            "Something went wrong while checking valid seats")))
        .map(m -> m.get("valid"));
  }

  public int isSeatValid(int rows, int columns, Seat seat) {
    if (seat.getRow() > rows || seat.getColumn() > columns) {
      return -1;
    }
    return !seat.getIsSpace() ? 1 : 0;
  }
}
