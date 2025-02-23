package com.punna.eventcatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punna.eventcatalog.dto.MovieDto;
import com.punna.eventcatalog.service.MovieService;
import java.io.IOException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.punna.commons.exception.EventApplicationException;
import org.punna.commons.validation.groups.UpdateGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movies")
public class MovieController {

  private final MovieService movieService;
  private final ObjectMapper objectMapper;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<MovieDto> createMovie(@RequestPart("movie") Mono<String> movieDtoStringMono,
      @RequestPart("images") Flux<FilePart> fileParts,
      @RequestPart("posterImage") Mono<FilePart> posterImageMono) throws IOException {
    return movieDtoStringMono.flatMap(dtoString -> {
      try {
        MovieDto movieDto = objectMapper.readValue(dtoString, MovieDto.class);
        return movieService.create(movieDto, fileParts, posterImageMono);
      } catch (IOException e) {
        return Mono.error(new EventApplicationException("Unable to parse movie dto",
            HttpStatus.BAD_REQUEST.value()));
      }
    });
  }

  @GetMapping("/{id}")
  public Mono<MovieDto> findById(@PathVariable String id) {
    return movieService.findById(id);
  }


  @GetMapping
  public Flux<MovieDto> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) Instant releaseDate) {
    if (title != null && !title.trim().isEmpty()) {
      return movieService.findAllByTitle(title, page, 10);
    }
    if (releaseDate != null) {
      return movieService.findByReleaseDate(releaseDate, page, 10);
    }
    return movieService.find(page, 10);
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteById(@PathVariable String id) {
    return movieService.deleteById(id);
  }


  @PatchMapping("/{id}")
  public Mono<MovieDto> updateById(@PathVariable String id,
      @Validated({UpdateGroup.class}) @RequestBody MovieDto movieDto) {
    return movieService.updateById(id, movieDto);
  }
}
