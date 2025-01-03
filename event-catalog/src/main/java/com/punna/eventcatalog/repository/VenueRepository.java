package com.punna.eventcatalog.repository;

import com.punna.eventcatalog.model.Event;
import com.punna.eventcatalog.model.Venue;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VenueRepository extends ReactiveMongoRepository<Venue, String > {
    Flux<Venue> findAllBy(Pageable pageable);

    @Query(value = "{ _id: '?0'}", fields = "{name: 1, _id: 0}")
    Mono<String> getSeatingLayoutId(String id);
}
