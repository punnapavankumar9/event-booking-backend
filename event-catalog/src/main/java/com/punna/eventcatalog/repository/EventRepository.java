package com.punna.eventcatalog.repository;

import com.punna.eventcatalog.model.Event;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends ReactiveMongoRepository<Event, ObjectId> {
}
