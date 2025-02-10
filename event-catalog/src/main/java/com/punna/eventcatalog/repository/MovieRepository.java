package com.punna.eventcatalog.repository;

import com.punna.eventcatalog.model.Movie;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {

    List<Movie> findByTitleContaining(String title, Sort sort);

    List<Movie> findByReleaseDate(LocalDateTime releaseDate);
}
