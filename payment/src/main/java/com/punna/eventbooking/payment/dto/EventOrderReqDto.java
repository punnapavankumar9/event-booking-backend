package com.punna.eventbooking.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EventOrderReqDto {

  @Null(message = "Id must be null")
  private String id;

  // from order service
  @NotNull(message = "OrderId must not be null")
  private String orderId;

  @NotNull(message = "amount must not be null")
  @Min(value = 0, message = "Order amount must not be less than 0")
  private BigDecimal amount;

}
