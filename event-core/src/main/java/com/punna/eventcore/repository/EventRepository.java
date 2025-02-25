package com.punna.eventcore.repository;

import com.punna.eventcore.dto.EventsForVenueProjection;
import com.punna.eventcore.dto.projections.EventShowListingProjection;
import com.punna.eventcore.model.Event;
import com.punna.eventcore.model.EventType;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface EventRepository extends ReactiveMongoRepository<Event, String> {

  Flux<Event> findAllBy(Pageable pageable);

  Flux<Event> findAllByEventType(EventType eventType);


  @Aggregation(pipeline = {
      // Stage 0: Matching
      """
          { $match: 
              { 
                eventId: ?0,
                openForBooking: true ,
                venueId: { $in: ?1 },
                'eventDurationDetails.startTime': { $gte: ?2, $lte: ?3 }
              }
          }""",
      //       Stage 1: projection
      """
          {
            $project: {
                        id: 1,
                        eventId: 1,
                        venueId: 1,
                        eventType: 1,
                        eventDurationDetails: 1,
                        pricingTierMaps: 1,
                        seatingLayoutId: 1,
                        numberOfBookedAndBlockedSeats: {
                         $sum: [
                            { $size: { $ifNull: ['$seatState.bookedSeats', []] } },
                            { $size: { $ifNull: ['$seatState.blockedSeats', []] } } 
                          ] 
                         }
                        } 
          }"""
  })
  Flux<EventShowListingProjection> findAllByEventIdAndVenueIdInAndEventDurationDetails_StartTimeBetween(
      String eventId,
      List<String> venueIds, Instant startTime, Instant endTime);

  Flux<EventsForVenueProjection> findAllByEventIdAndVenueId(String eventId, String venueId,
      Sort sort);
}
