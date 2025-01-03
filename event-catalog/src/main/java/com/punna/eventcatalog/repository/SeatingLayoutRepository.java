package com.punna.eventcatalog.repository;

import com.punna.eventcatalog.model.SeatLocation;
import com.punna.eventcatalog.model.SeatingLayout;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

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
    Mono<Map<String, Boolean>> validatedSelectedSeatsAreValid(String id, List<SeatLocation> seatLocations);
}