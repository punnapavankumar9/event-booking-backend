package com.punna.eventcore.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.punna.commons.validation.groups.CreateGroup;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Seat {

  @NotNull(message = "isSpace must not be null", groups = CreateGroup.class)
  private Boolean isSpace;

  @NotNull(message = "row must not be null", groups = CreateGroup.class)
  @Min(message = "row must be positive", value = 0)
  private Integer row;

  @NotNull(message = "column must not be null", groups = CreateGroup.class)
  @Min(message = "column must be positive", value = 0)
  private Integer column;

  @NotNull(message = "tier name must not be null", groups = CreateGroup.class)
  private String tier;
}
