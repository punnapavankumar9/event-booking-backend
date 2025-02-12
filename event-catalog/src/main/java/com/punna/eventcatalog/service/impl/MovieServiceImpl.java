package com.punna.eventcatalog.service.impl;

import com.punna.eventcatalog.dto.MovieDto;
import com.punna.eventcatalog.mapper.MovieMapper;
import com.punna.eventcatalog.model.Movie;
import com.punna.eventcatalog.repository.MovieRepository;
import com.punna.eventcatalog.service.MovieService;
import com.punna.eventcatalog.service.StorageService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.punna.commons.exception.EntityNotFoundException;
import org.punna.commons.exception.EventApplicationException;
import org.punna.commons.validation.groups.CreateGroup;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

  private final MovieRepository movieRepository;
  private final ReactiveMongoTemplate reactiveMongoTemplate;
  private final Validator validator;
  private final StorageService storageService;

  @Override
  public Mono<MovieDto> findById(String id) {
    return movieRepository.findById(id)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(Movie.class.getSimpleName(), id)))
        .map(MovieMapper::toMovieDto);
  }

  @Override
  public Flux<MovieDto> findAllByTitle(String title, int page, int size) {
    return movieRepository.findByTitleContaining(title,
            PageRequest.of(page, size, Sort.by("rating").descending()))
        .map(MovieMapper::toMovieDto);
  }

  @Override
  public Flux<MovieDto> findByReleaseDate(LocalDateTime releaseDate, int page, int size) {
    return movieRepository.findByReleaseDate(releaseDate, PageRequest.of(page, size))
        .map(MovieMapper::toMovieDto);
  }

  public Flux<MovieDto> find(int page, int size) {
    return movieRepository.findByIdNotNull(PageRequest.of(page, size))
        .map(MovieMapper::toMovieDto);
  }

  @Override
  public Mono<Void> deleteById(String id) {
    Query query = new Query(Criteria.where("_id").is(id));

    return reactiveMongoTemplate.findAndRemove(query, Movie.class)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(Movie.class.getSimpleName(), id)))
        .flatMap(movie -> Mono.empty());
  }

  public Mono<MovieDto> create(MovieDto movieDto, Flux<FilePart> filePartFlux) {

    Set<ConstraintViolation<MovieDto>> violations = validator.validate(movieDto,
        CreateGroup.class);
    if (!violations.isEmpty()) {
      return Mono.error(new ConstraintViolationException(violations));
    }
    if (movieDto.getImageUrls() == null) {
      movieDto.setImageUrls(new ArrayList<>());
    }

    return filePartFlux.collectList().flatMap(fileParts -> {
      if (fileParts.isEmpty()) {
        return Mono.error(new EventApplicationException("At least one image is required"));
      }
      return Flux.fromIterable(fileParts).flatMap(
          filePart -> storageService.storeImage(filePart)
              .map(filePath -> movieDto.getImageUrls().add(filePath))).then(Mono.empty());
    }).then(movieRepository.save(MovieMapper.toMovie(movieDto))
        .map(MovieMapper::toMovieDto));
  }

  @Override
  public Mono<MovieDto> updateById(String id, MovieDto movieDto) {
    return movieRepository.findById(id)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(Movie.class.getSimpleName(), id)))
        .flatMap(movie -> {
          MovieMapper.merge(movie, movieDto);
          return movieRepository.save(movie);
        }).map(MovieMapper::toMovieDto);
  }
}
