package com.punna.order.mapper;

import com.punna.order.dto.OrderReqDto;
import com.punna.order.dto.OrderResDto;
import com.punna.order.dto.OrderStatus;
import com.punna.order.model.Order;

public class OrderMapper {


  public static OrderResDto toDto(Order order) {
    return OrderResDto.builder()
        .id(order.getId())
        .info(order.getInfo())
        .createdDate(order.getCreatedDate())
        .createdBy(order.getCreatedBy())
        .amount(order.getAmount())
        .eventId(order.getEventId())
        .eventOrderId(order.getEventOrderId())
        .eventType(order.getEventType())
        .orderStatus(order.getOrderStatus())
        .build();
  }

  public static OrderResDto toDto(Order order, String razorOrderId) {
    return OrderResDto.builder()
        .id(order.getId())
        .info(order.getInfo())
        .createdDate(order.getCreatedDate())
        .createdBy(order.getCreatedBy())
        .amount(order.getAmount())
        .eventId(order.getEventId())
        .eventOrderId(order.getEventOrderId())
        .eventType(order.getEventType())
        .orderStatus(order.getOrderStatus())
        .razorOrderId(razorOrderId)
        .build();
  }


  public static Order toEntity(OrderReqDto orderReqDto, String paymentId, OrderStatus orderStatus) {
    return Order.builder()
        .id(orderReqDto.id())
        .amount(orderReqDto.amount())
        .eventId(orderReqDto.eventId())
        .eventType(orderReqDto.eventType())
        .eventOrderId(paymentId)
        .info(orderReqDto.info())
        .orderStatus(orderStatus)
        .build();
  }
}
