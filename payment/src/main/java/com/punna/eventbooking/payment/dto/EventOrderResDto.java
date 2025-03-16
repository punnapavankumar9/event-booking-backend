package com.punna.eventbooking.payment.dto;

import com.punna.eventbooking.payment.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EventOrderResDto extends EventOrderReqDto {

  private OrderStatus orderStatus;

  private String paymentIntegratorOrderId;

}

