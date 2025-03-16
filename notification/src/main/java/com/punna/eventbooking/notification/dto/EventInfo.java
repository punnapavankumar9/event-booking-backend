package com.punna.eventbooking.notification.dto;

import lombok.Builder;

@Builder
public record EventInfo(
    EventBasicProjection event,
    VenueIdAndNameProjection venue
) {

}
