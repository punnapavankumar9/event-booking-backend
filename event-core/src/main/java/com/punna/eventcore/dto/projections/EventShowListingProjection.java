package com.punna.eventcore.dto.projections;

import com.punna.eventcore.model.EventDurationDetails;
import com.punna.eventcore.model.EventType;
import com.punna.eventcore.model.PricingTierMap;
import java.util.List;

public record EventShowListingProjection(
    String id,
    String eventId,
    String venueId,
    EventType eventType,
    EventDurationDetails eventDurationDetails,
    List<PricingTierMap> pricingTierMaps,
    String seatingLayoutId,
    Integer numberOfBookedAndBlockedSeats
) {

}
