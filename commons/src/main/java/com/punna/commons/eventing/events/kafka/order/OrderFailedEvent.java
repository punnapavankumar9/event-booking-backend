package com.punna.commons.eventing.events.kafka.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.Builder;

@Builder
public record OrderFailedEvent(
    String id,
    List<String> seats,
    BigDecimal amount,
    Instant orderedAt,
    String eventId,
    String createdBy
) {

}
