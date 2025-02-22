package com.punna.eventcore.repository;

import com.punna.eventcore.dto.projections.EventSeatsProjection;
import com.punna.eventcore.model.SeatLocation;
import com.punna.eventcore.model.SeatingLayout;
import java.util.List;
import java.util.Map;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SeatingLayoutRepository extends ReactiveMongoRepository<SeatingLayout, String> {

  @Aggregation(
      pipeline = {
          """
              {
                  $match: {
                      _id: ?0, 
                      seats:
                          {
                              $elemMatch: {
                                  "isSpace" :false
                              }
                          }
                      }
              }
              """,
          "{ $match: {_id: ?0, seats: {$elemMatch: {\"isSpace\": false}} } }",
          "{ $project: { seats: { $map:  {input: '$seats', as:  'seat', in:  {row: '$$seat.row', column :  '$$seat.column'}}}, _id: 0 }}",
          "{ $project: { valid: { $eq: [ {$size: {$setIntersection: ['$seats', {$literal: ?1}]}} , {$size: {$literal: ?1}} ] } }}"
      }
  )
  Mono<Map<String, Boolean>> validatedSelectedSeatsAreValid(String id,
      List<SeatLocation> seatLocations);

  Flux<SeatingLayout> findByName(String name);

  Mono<EventSeatsProjection> findEventSeatsProjectionById(String id);
}
