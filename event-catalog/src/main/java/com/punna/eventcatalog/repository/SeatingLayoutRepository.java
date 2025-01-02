package com.punna.eventcatalog.repository;

import com.punna.eventcatalog.model.SeatingLayout;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatingLayoutRepository extends ReactiveMongoRepository<SeatingLayout, String> {
}
