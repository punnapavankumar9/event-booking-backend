package com.punna.eventcore.dto.projections;

public record VenueNameWithLayoutIdProjection(
    String name,
    String id,
    String seatingLayoutId
) {

}
