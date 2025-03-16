package com.punna.eventcore.dto.projections;

import lombok.Builder;

@Builder
public record EventNameAndIdProjection(
    String id,
    String name
) {

}
