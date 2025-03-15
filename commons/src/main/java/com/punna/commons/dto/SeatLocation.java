package com.punna.commons.dto;

import lombok.Builder;

@Builder
public record SeatLocation(
    Integer row,
    Integer column
) {

}
