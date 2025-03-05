package com.punna.order.dto;


import com.punna.order.model.EventType;
import com.punna.order.model.OrderInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@Builder
public record OrderReqDto(

    @NotNull(message = "OrderInfo must not be null")
    OrderInfo info,

    @Null(message = "Id must be null")
    String id,

    @NotNull(message = "eventId Must not be null")
    String eventId,

    @NotNull(message = "amount Must not be null")
    @Min(value = 0, message = "amount must not be less than 0")
    BigDecimal amount,

    @NotNull
    EventType eventType
) {

}
