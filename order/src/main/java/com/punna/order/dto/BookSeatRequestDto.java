package com.punna.order.dto;

import com.punna.order.model.SeatLocation;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;

@Builder
public record BookSeatRequestDto(
    BigDecimal amount,
    List<SeatLocation> seats
) {

}
