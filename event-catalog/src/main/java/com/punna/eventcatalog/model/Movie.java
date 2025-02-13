package com.punna.eventcatalog.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

  @Id
  private String id;

  private String title;

  private LocalDateTime releaseDate;

  private List<String> genres;

  private String description;

  private Float rating;

  private List<String> imageUrls;

  private Integer likes;

  // minutes
  private Integer duration;


  // movie tags like, 2D, 3D, Telugu Hindi, UA, A, U
  private List<String> tags;

}
