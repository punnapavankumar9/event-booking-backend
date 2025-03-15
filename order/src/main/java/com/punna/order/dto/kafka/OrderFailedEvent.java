package com.punna.order.dto.kafka;

import lombok.Builder;

@Builder
public record OrderFailedEvent(
    String id
) {

}
