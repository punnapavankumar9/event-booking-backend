package com.punna.eventcatalog.repository;

import com.punna.eventcatalog.model.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface MovieRepository extends ReactiveCrudRepository<Movie, String> {

    Flux<Movie> findByTitleContaining(String title, Pageable pageable);

    Flux<Movie> findByReleaseDate(LocalDateTime releaseDate, Pageable pageable);

    Flux<Movie> findByIdNotNull(Pageable pageable);
}
