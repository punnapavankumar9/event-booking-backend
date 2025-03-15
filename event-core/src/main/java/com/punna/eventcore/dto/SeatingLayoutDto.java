package com.punna.eventcore.dto;


import com.punna.eventcore.model.Seat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.punna.commons.validation.groups.CreateGroup;
import com.punna.commons.validation.groups.UpdateGroup;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatingLayoutDto {

  @NotNull(message = "id must not be null", groups = UpdateGroup.class)
  @Null(message = "id must not be provided", groups = CreateGroup.class)
  private String id;

  @NotNull(message = "Name must not be null", groups = CreateGroup.class)
  @Size(min = 1, max = 100)
  private String name;

  @NotNull(message = "capacity must not be null", groups = CreateGroup.class)
  @Positive(message = "capacity must be positive")
  private Integer capacity;

  @NotNull(message = "rows must not be null", groups = CreateGroup.class)
  @Min(message = "rows must be positive", value = 0)
  private Integer rows;

  @NotNull(message = "columns must not be null", groups = CreateGroup.class)
  @Min(message = "columns must be positive", value = 0)
  private Integer columns;

  @NotNull(message = "Screen position must not be null", groups = CreateGroup.class)
  private ScreenPosition screenPosition;

  @Valid
  private List<Seat> seats;
}
