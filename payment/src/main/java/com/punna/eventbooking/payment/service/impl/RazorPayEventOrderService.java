package com.punna.eventbooking.payment.service.impl;

import com.punna.eventbooking.payment.dto.EventOrderReqDto;
import com.punna.eventbooking.payment.dto.EventOrderResDto;
import com.punna.eventbooking.payment.mapper.EventOrderMapper;
import com.punna.eventbooking.payment.model.EventOrder;
import com.punna.eventbooking.payment.model.OrderStatus;
import com.punna.eventbooking.payment.repository.EventOrderRepository;
import com.punna.eventbooking.payment.service.EventOrderService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RazorPayEventOrderService implements EventOrderService {

  private final EventOrderRepository eventOrderRepository;
  private final RazorpayClient razorPayClient;


  @SneakyThrows
  @PreAuthorize("isAuthenticated()")
  public EventOrderResDto createOrder(EventOrderReqDto eventOrderReqDto) {
    JSONObject orderReq = new JSONObject();
    orderReq.put("amount", eventOrderReqDto.getAmount().multiply(BigDecimal.valueOf(100)));
    orderReq.put("currency", "INR");
    orderReq.put("receipt", eventOrderReqDto.getOrderId());
    Order razorPayOrder = razorPayClient.orders.create(orderReq);

    EventOrder order = EventOrder.builder().razorPayOrderId(razorPayOrder.get("id"))
        .orderId(eventOrderReqDto.getOrderId()).amount(eventOrderReqDto.getAmount())
        .orderStatus(OrderStatus.PENDING).build();
    EventOrder savedOrder = eventOrderRepository.save(order);
    return EventOrderMapper.toEventOrderResDto(savedOrder);
  }
}
