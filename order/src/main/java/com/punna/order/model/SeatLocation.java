package com.punna.order.model;

import lombok.Builder;

@Builder
public record SeatLocation(Integer row, Integer column) {

}
