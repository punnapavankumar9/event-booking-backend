package com.punna.eventcore.dto;


import com.punna.eventcore.model.Seat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.punna.commons.validation.groups.CreateGroup;
import org.punna.commons.validation.groups.UpdateGroup;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatingLayoutDto {

  @NotNull(message = "id must not be null", groups = UpdateGroup.class)
  @Null(message = "id must not be provided", groups = CreateGroup.class)
  private String id;

  @NotNull(message = "capacity must not be null", groups = CreateGroup.class)
  @Positive(message = "capacity must be positive")
  private Integer capacity;

  @NotNull(message = "rows must not be null", groups = CreateGroup.class)
  @Positive(message = "rows must be positive")
  private Integer rows;

  @NotNull(message = "columns must not be null", groups = CreateGroup.class)
  @Positive(message = "columns must be positive")
  private Integer columns;

  @NotNull(message = "Screen position must not be null", groups = CreateGroup.class)
  private ScreenPosition screenPosition;

  @Valid
  private List<Seat> seats;
}
