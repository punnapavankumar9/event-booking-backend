package com.punna.eventcore.repository;

import com.punna.eventcore.model.Event;
import com.punna.eventcore.model.EventType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface EventRepository extends ReactiveMongoRepository<Event, String> {

  Flux<Event> findAllBy(Pageable pageable);

  Flux<Event> findAllByEventType(EventType eventType);
}
