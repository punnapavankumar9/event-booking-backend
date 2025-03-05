package com.punna.order.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfo {

  @NotNull(message = "seats must not be null")
  @Size(message = "seats must ")
  List<SeatLocation> seats;

  @NotNull(message = "seat Labels must not be null")
  @Size(message = "seat labels must be greater than 1", min = 1)
  List<String> seatLabels;
}
