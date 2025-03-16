package com.punna.eventbooking.payment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  // from order service
  @NotNull
  private String orderId;

  @NotNull
  @Min(value = 0, message = "Order amount must not be less than 0")
  private BigDecimal amount;

  @NotNull
  private OrderStatus orderStatus;

  @NotNull
  private String paymentIntegratorOrderId;
}
