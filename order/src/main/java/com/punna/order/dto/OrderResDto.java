package com.punna.order.dto;

import com.punna.order.model.EventType;
import com.punna.order.model.OrderInfo;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;

@Builder
public record OrderResDto(

    OrderInfo info,

    String id,

    String eventId,

    BigDecimal amount,

    EventType eventType,

    String eventOrderId,

    String createdBy,

    Instant createdDate,

    OrderStatus orderStatus,

    String razorOrderId

) {

}
