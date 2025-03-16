package com.punna.eventcore.dto;

import com.punna.eventcore.dto.projections.EventBasicProjection;
import com.punna.eventcore.dto.projections.VenueIdAndNameProjection;
import lombok.Builder;

@Builder
public record EventInfo(
    EventBasicProjection event,
    VenueIdAndNameProjection venue
) {

}
