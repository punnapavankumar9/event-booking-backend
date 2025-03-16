package com.punna.eventbooking.notification.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderEmailDto {

  private String id;
  private List<String> seats;
  private BigDecimal amount;
  private Instant orderedAt;
}
