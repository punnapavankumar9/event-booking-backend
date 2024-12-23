package com.punna.eventcatalog.repository;

import com.punna.eventcatalog.model.Event;
import com.punna.eventcatalog.model.Venue;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface VenueRepository extends ReactiveMongoRepository<Venue, String > {
    Flux<Venue> findAllBy(Pageable pageable);

}
