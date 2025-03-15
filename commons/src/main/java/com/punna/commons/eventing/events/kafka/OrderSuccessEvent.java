package com.punna.commons.eventing.events.kafka;

import lombok.Builder;

@Builder
public record OrderSuccessEvent(
    String id
) {

}
