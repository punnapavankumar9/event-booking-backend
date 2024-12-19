package com.punna.eventcatalog.repository;

import com.punna.eventcatalog.model.Venue;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends ReactiveMongoRepository<Venue, ObjectId> {
}
