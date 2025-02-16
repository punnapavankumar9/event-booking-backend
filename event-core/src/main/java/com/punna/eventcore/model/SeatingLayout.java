package com.punna.eventcore.model;

import com.punna.eventcore.dto.ScreenPosition;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("seating_layout")
public class SeatingLayout {

  @Id
  private String id;

  @Indexed(unique = true)
  private String name;

  private Integer capacity;

  private Integer rows;

  private Integer columns;

  private ScreenPosition screenPosition;

  private List<Seat> seats;
}
