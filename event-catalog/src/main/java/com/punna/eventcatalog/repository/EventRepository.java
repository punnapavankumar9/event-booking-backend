package com.punna.eventcatalog.repository;

import com.punna.eventcatalog.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface EventRepository extends ReactiveMongoRepository<Event, String> {

    Flux<Event> findAllBy(Pageable pageable);
}
