package com.punna.eventcore.dto;

import com.punna.eventcore.model.SeatLocation;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;

@Builder
public record BookSeatRequestDto(
    BigDecimal amount,
    List<SeatLocation> seats
) {

}
