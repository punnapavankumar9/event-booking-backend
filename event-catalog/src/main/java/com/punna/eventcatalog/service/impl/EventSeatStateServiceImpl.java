package com.punna.eventcatalog.service.impl;

import com.punna.eventcatalog.model.Event;
import com.punna.eventcatalog.model.SeatLocation;
import com.punna.eventcatalog.service.EventSeatStateService;
import com.punna.eventcatalog.service.EventService;
import com.punna.eventcatalog.service.SeatingLayoutService;
import lombok.RequiredArgsConstructor;
import org.punna.commons.exception.EventApplicationException;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventSeatStateServiceImpl implements EventSeatStateService {
    private final ReactiveMongoTemplate mongoTemplate;

    private final String bookedSeatsKey = "seatState.bookedSeats";
    private final String blockedSeatsKey = "seatState.blockedSeats";
    private final EventService eventService;
    private final SeatingLayoutService seatingLayoutService;

    @Override
    public Mono<Void> bookSeats(String eventId, List<SeatLocation> seatLocations) {
        // check if the seats are not already booked.
        Query checkAlreadyBookedQuery = new Query(Criteria
                .where("_id")
                .is(eventId)).addCriteria(new Criteria().orOperator(seatLocations
                .stream()
                .map(seat -> Criteria
                        .where(bookedSeatsKey)
                        .elemMatch(Criteria
                                .where("row")
                                .is(seat.getRow())
                                .and("column")
                                .is(seat.getColumn())))
                .toArray(Criteria[]::new)));

        // check if the seats are blocked
        Query checkBlockedQuery = new Query(Criteria
                .where("_id")
                .is(eventId)).addCriteria(new Criteria().orOperator(seatLocations
                .stream()
                .map(seat -> Criteria
                        .where(blockedSeatsKey)
                        .elemMatch(Criteria
                                .where("row")
                                .is(seat.getRow())
                                .and("column")
                                .is(seat.getColumn())))
                .toArray(Criteria[]::new)));

        // update query
        Query query = new Query(Criteria
                .where("_id")
                .is(eventId));
        Update update = new Update()
                .addToSet(bookedSeatsKey)
                .each(seatLocations);
        return Mono
                .zip(mongoTemplate.count(checkAlreadyBookedQuery, Event.class),
                        mongoTemplate.count(checkBlockedQuery, Event.class),
                        areSelectedSeatsValid(eventId, seatLocations)
                    )
                .flatMap(tuple -> {
                    if (tuple.getT1() > 0) {
                        return Mono.error(new EventApplicationException("Few of the tickets are already booked", 409));
                    } else if (tuple.getT2() > 0) {
                        return Mono.error(new EventApplicationException("Few of the tickets are already blocked", 409));
                    } else if (!tuple.getT3()) {
                        return Mono.error(new EventApplicationException("selected seats are containing invalid location",
                                400
                        ));
                    }
                    return mongoTemplate
                            .updateFirst(query, update, Event.class)
                            .filter(r -> r.getModifiedCount() != 0)
                            .switchIfEmpty(Mono.error(new EventApplicationException("Unable to book seats")))
                            .flatMap(r -> Mono.empty());
                });
    }

    @Override
    public Mono<Void> blockSeats(String eventId, List<SeatLocation> seatLocations) {
        Query query = Query.query(Criteria
                .where("_id")
                .is(eventId));
        Update update = new Update()
                .addToSet(blockedSeatsKey)
                .each(seatLocations);

        Query checkAlreadyBookedQuery = new Query(Criteria
                .where("_id")
                .is(eventId)).addCriteria(new Criteria().orOperator(seatLocations
                .stream()
                .map(seat -> Criteria
                        .where(bookedSeatsKey)
                        .elemMatch(Criteria
                                .where("row")
                                .is(seat.getRow())
                                .and("column")
                                .is(seat.getColumn())))
                .toArray(Criteria[]::new)));

        return Mono
                .zip(mongoTemplate.count(checkAlreadyBookedQuery, Event.class),
                        areSelectedSeatsValid(eventId, seatLocations)
                    )
                .flatMap(tuple2 -> {
                    if (tuple2.getT1() > 0) {
                        return Mono.error(new EventApplicationException("Few of the tickets are already booked", 409));
                    } else if (!tuple2.getT2()) {
                        return Mono.error(new EventApplicationException("selected seats are containing invalid location",
                                400
                        ));
                    }
                    return mongoTemplate
                            .updateFirst(query, update, Event.class)
                            .filter(r -> r.getModifiedCount() != 0)
                            .switchIfEmpty(Mono.error(new EventApplicationException("Unable to block seats")))
                            .flatMap(r -> Mono.empty());
                });
    }

    @Override
    public Mono<Void> unblockSeats(String eventId, List<SeatLocation> seatLocations) {
        Query query = Query.query(Criteria
                .where("_id")
                .is(eventId));
        Update update = new Update().pullAll(blockedSeatsKey, seatLocations.toArray());

        return areSelectedSeatsValid(eventId, seatLocations).flatMap(valid -> {
            if (!valid) {
                return Mono.error(new EventApplicationException("selected seats are containing invalid location", 400));
            }
            return mongoTemplate
                    .updateFirst(query, update, Event.class)
                    .filter(r -> r.getModifiedCount() != 0)
                    .switchIfEmpty(Mono.error(new EventApplicationException("Unable to unblock seats")))
                    .flatMap(r -> Mono.empty());
        });
    }

    @Override
    public Mono<Void> unBookSeats(String eventId, List<SeatLocation> seatLocations) {
        Query query = Query.query(Criteria
                .where("_id")
                .is(eventId));

        Update update = new Update().pullAll(bookedSeatsKey, seatLocations.toArray());

        return areSelectedSeatsValid(eventId, seatLocations).flatMap(valid -> {
            if (!valid) {
                return Mono.error(new EventApplicationException("selected seats are containing invalid location", 400));
            }
            return mongoTemplate
                    .updateFirst(query, update, Event.class)
                    .filter(r -> r.getModifiedCount() != 0)
                    .switchIfEmpty(Mono.error(new EventApplicationException("Unable to unbook seats")))
                    .flatMap(r -> Mono.empty());
        });
    }

    @Override
    public Mono<Boolean> areSelectedSeatsValid(String eventId, List<SeatLocation> seatLocations) {
        return eventService
                .findById(eventId)
                .flatMap(event -> seatingLayoutService.areSelectedSeatsValid(event.getSeatingLayoutId(),
                        seatLocations
                                                                            ));
    }
}
