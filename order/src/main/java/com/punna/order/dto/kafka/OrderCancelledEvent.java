package com.punna.order.dto.kafka;

import com.punna.order.model.EventType;
import com.punna.order.model.OrderInfo;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;

@Builder
public record OrderCancelledEvent(
    String id,

    OrderInfo info,

    String eventId,

    BigDecimal amount,

    EventType eventType,

    String eventOrderId,

    String createdBy,

    Instant createdDate
) {

}
