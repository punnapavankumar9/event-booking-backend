package com.punna.eventcore.dto.projections;

import com.punna.eventcore.model.Seat;
import java.util.List;

public record EventSeatsProjection(
    List<Seat> seats
) {

}
