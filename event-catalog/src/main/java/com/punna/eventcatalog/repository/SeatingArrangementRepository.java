package com.punna.eventcatalog.repository;

import com.punna.eventcatalog.model.SeatingArrangement;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatingArrangementRepository extends ReactiveMongoRepository<SeatingArrangement, String> {
}
