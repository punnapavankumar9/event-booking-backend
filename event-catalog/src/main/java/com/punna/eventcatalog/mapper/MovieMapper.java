package com.punna.eventcatalog.mapper;

import com.punna.eventcatalog.dto.MovieDto;
import com.punna.eventcatalog.model.Movie;

public class MovieMapper {

    public static Movie toMovie(MovieDto movieDto) {
        return Movie.builder()
                    .title(movieDto.getTitle())
                    .releaseDate(movieDto.getReleaseDate())
                    .genres(movieDto.getGenres())
                    .id(movieDto.getId())
                    .description(movieDto.getDescription())
                    .imageUrls(movieDto.getImageUrls())
                    .rating(movieDto.getRating())
                    .build();
    }

    public static MovieDto toMovieDto(Movie movie) {
        return MovieDto.builder()
                       .title(movie.getTitle())
                       .releaseDate(movie.getReleaseDate())
                       .genres(movie.getGenres())
                       .id(movie.getId())
                       .description(movie.getDescription())
                       .imageUrls(movie.getImageUrls())
                       .rating(movie.getRating())
                       .build();
    }

    public static void merge(Movie movie, MovieDto movieDto) {
        if (movieDto.getTitle() != null) {
            movie.setTitle(movieDto.getTitle());
        }
        if (movieDto.getReleaseDate() != null) {
            movie.setReleaseDate(movieDto.getReleaseDate());
        }
        if (movieDto.getGenres() != null) {
            movie.setGenres(movieDto.getGenres());
        }
        if (movieDto.getId() != null) {
            movie.setId(movieDto.getId());
        }
        if (movieDto.getDescription() != null) {
            movie.setDescription(movieDto.getDescription());
        }
        if (movieDto.getImageUrls() != null) {
            movie.setImageUrls(movieDto.getImageUrls());
        }
        if (movieDto.getRating() != null) {
            movie.setRating(movieDto.getRating());
        }
    }

}
