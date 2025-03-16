package com.punna.eventcore.repository;

import com.punna.eventcore.dto.projections.VenueIdAndNameProjection;
import com.punna.eventcore.dto.projections.VenueNameWithLayoutIdProjection;
import com.punna.eventcore.model.Venue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VenueRepository extends ReactiveMongoRepository<Venue, String> {

  Flux<Venue> findAllBy(Pageable pageable);

  Flux<VenueIdAndNameProjection> findAllByCityEqualsIgnoreCase(String city);

  Flux<VenueNameWithLayoutIdProjection> findAllByNameContainingIgnoreCase(String name,
      Pageable pageable);

  @Query(value = "{_id: ?0}", fields = "{_id:  1, name:  1, seatingLayoutId:  1, }")
  Mono<VenueNameWithLayoutIdProjection> findByIdVenueNameAndLayoutId(String id);

  <T> Mono<T> findById(String id, Class<T> projection);
}
