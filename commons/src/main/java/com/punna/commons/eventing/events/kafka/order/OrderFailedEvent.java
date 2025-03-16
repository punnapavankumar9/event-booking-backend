package com.punna.commons.eventing.events.kafka.order;

import lombok.Builder;

@Builder
public record OrderFailedEvent(
    String id
) {

}
