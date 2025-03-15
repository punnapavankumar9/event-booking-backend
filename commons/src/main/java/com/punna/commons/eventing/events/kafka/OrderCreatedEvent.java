package com.punna.commons.eventing.events.kafka;


import lombok.Builder;


@Builder
public record OrderCreatedEvent(
    String id
) {

}
