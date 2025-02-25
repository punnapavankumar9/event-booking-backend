package com.punna.eventcore.repository;

import com.punna.eventcore.dto.projections.VenueIdAndNameProjection;
import com.punna.eventcore.dto.projections.VenueNameWithLayoutIdProjection;
import com.punna.eventcore.model.Venue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface VenueRepository extends ReactiveMongoRepository<Venue, String> {

  Flux<Venue> findAllBy(Pageable pageable);

  Flux<VenueIdAndNameProjection> findAllByCityEqualsIgnoreCase(String city);

  Flux<VenueNameWithLayoutIdProjection> findAllByNameContainingIgnoreCase(String name,
      Pageable pageable);
}
