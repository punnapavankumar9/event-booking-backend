package com.punna.eventbooking.payment.mapper;

import com.punna.eventbooking.payment.dto.EventOrderReqDto;
import com.punna.eventbooking.payment.dto.EventOrderResDto;
import com.punna.eventbooking.payment.model.EventOrder;

public class EventOrderMapper {

  public static EventOrder toEventOrder(EventOrderReqDto eventOrderReqDto) {
    return EventOrder.builder()
        .orderId(eventOrderReqDto.getOrderId())
        .amount(eventOrderReqDto.getAmount())
        .build();
  }

  public static EventOrderResDto toEventOrderResDto(EventOrder eventOrder) {
    return EventOrderResDto.builder()
        .orderId(eventOrder.getOrderId())
        .amount(eventOrder.getAmount())
        .razorPayOrderId(eventOrder.getRazorPayOrderId())
        .orderStatus(eventOrder.getOrderStatus())
        .id(eventOrder.getId())
        .build();
  }
}
