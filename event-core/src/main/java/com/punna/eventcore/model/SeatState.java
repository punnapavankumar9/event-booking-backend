package com.punna.eventcore.model;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatState {

  @Valid
  private List<SeatLocation> blockedSeats;

  @Valid
  private List<SeatLocation> bookedSeats;
}
