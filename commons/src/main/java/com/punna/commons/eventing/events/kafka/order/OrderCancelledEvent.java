package com.punna.commons.eventing.events.kafka.order;

import lombok.Builder;

@Builder
public record OrderCancelledEvent(
    String id
) {

}
