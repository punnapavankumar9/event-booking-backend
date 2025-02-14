package com.punna.eventcatalog.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.punna.commons.validation.groups.CreateGroup;
import org.punna.commons.validation.groups.UpdateGroup;

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
  private LocalDateTime releaseDate;

  @Size(min = 1, max = 5)
  private List<String> genres;

  @Size(min = 10, max = 512)
  private String description;

  @Min(message = "rating must be at least 1", value = 1)
  @Max(message = "rating must not be greater than 10", value = 10)
  private Float rating;

  private List<String> imageUrls;

  @Min(message = "likes must be positive", value = 0)
  private Integer likes = 0;

  // minutes
  @Min(message = "duration must be greater 10 min", value = 10)
  private Integer duration;

  // movie tags like, 2D, 3D, Telugu Hindi, UA, A, U
  private List<String> tags;

  @NotNull(message = "poster must not be null", groups = CreateGroup.class)
  private String posterUrl;
}
