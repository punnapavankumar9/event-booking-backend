package com.punna.eventcore.dto;

import java.time.Instant;
import java.util.List;
import lombok.Builder;

@Builder
public record BookingPageInfo(
    String venueName,
    EventResponseDto event,
    SeatingLayoutDto seatingLayout
) {

}
