package com.punna.eventcatalog.service;


import com.punna.eventcatalog.dto.MovieDto;
import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieService {

    Mono<MovieDto> findById(String id);

    Flux<MovieDto> findAllByTitle(String title, int page, int size);

    Flux<MovieDto> findByReleaseDate(LocalDateTime releaseDate, int page, int size);

    Flux<MovieDto> find(int page, int size);

    Mono<Void> deleteById(String id);

    Mono<MovieDto> updateById(String id, MovieDto movieDto);

    Mono<MovieDto> create(MovieDto movieDto, Flux<FilePart> fileParts) throws IOException;
}
