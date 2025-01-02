package com.punna.eventcatalog.model;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
