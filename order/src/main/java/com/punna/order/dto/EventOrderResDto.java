package com.punna.order.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventOrderResDto {

  private String id;

  private String orderId;

  private BigDecimal amount;

  private OrderStatus orderStatus;

  private String paymentIntegratorOrderId;
}
