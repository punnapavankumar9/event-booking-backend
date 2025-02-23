package com.punna.eventcore.dto;

public record EventsForVenueProjection(
    String id,
    EventDurationDetailsDto eventDurationDetails,
    String venueId,
    Boolean openForBooking
) {

}
