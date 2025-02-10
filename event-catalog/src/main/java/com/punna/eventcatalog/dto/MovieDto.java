package com.punna.eventcatalog.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.punna.commons.validation.groups.CreateGroup;
import org.punna.commons.validation.groups.UpdateGroup;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {

    @NotNull(message = "id must not be null for update", groups = UpdateGroup.class)
    @Null(message = "id is not required for create operation", groups = CreateGroup.class)
    private String id;

    @NotNull(message = "movies title is required", groups = CreateGroup.class)
    private String title;

    @NotNull(message = "release data is required", groups = CreateGroup.class)
    @Future(message = "ReleaseData must be a future data")
    private LocalDateTime releaseDate;

    @Size(min = 1, max = 5)
    private List<String> genres;

    @Size(min = 10, max = 512)
    private String description;

    @NotNull(message = "rating must no be null", groups = CreateGroup.class)
    @Min(message = "rating must be at least 1", value = 1)
    @Max(message = "rating must not be greater than 10", value = 10)
    private float rating;

    @Size(min = 1, max = 5, message = "ImageUrls must not be null and must not be more than 5")
    private List<String> imageUrls;
}
