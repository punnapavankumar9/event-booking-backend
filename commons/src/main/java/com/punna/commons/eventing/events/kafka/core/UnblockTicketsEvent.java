package com.punna.commons.eventing.events.kafka.core;

import com.punna.commons.dto.SeatLocation;
import java.util.List;
import lombok.Builder;

@Builder
public record UnblockTicketsEvent(
    List<SeatLocation> seatLocations,
    String id
) {

}
