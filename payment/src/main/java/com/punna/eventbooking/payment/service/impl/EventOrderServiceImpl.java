package com.punna.eventbooking.payment.service.impl;

import com.punna.eventbooking.payment.dto.EventOrderReqDto;
import com.punna.eventbooking.payment.dto.EventOrderResDto;
import com.punna.eventbooking.payment.mapper.EventOrderMapper;
import com.punna.eventbooking.payment.model.EventOrder;
import com.punna.eventbooking.payment.model.OrderStatus;
import com.punna.eventbooking.payment.repository.EventOrderRepository;
import com.punna.eventbooking.payment.service.EventOrderService;
import com.punna.eventbooking.payment.service.PaymentIntegratorService;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventOrderServiceImpl implements EventOrderService {

  private final PaymentIntegratorService paymentIntegratorService;

  private final EventOrderRepository eventOrderRepository;

  @Override
  public EventOrderResDto createOrder(EventOrderReqDto eventOrderReqDto) {
    String orderId = paymentIntegratorService.createOrder(eventOrderReqDto);
    EventOrder order = EventOrder.builder()
        .orderId(eventOrderReqDto.getOrderId()).amount(eventOrderReqDto.getAmount())
        .paymentIntegratorOrderId(orderId)
        .orderStatus(OrderStatus.PENDING).build();
    EventOrder savedOrder = eventOrderRepository.save(order);
    return EventOrderMapper.toEventOrderResDto(savedOrder);
  }

  @Override
  public void initiateRefund(EventOrder eventOrder) throws RazorpayException {
    String refundId = paymentIntegratorService.initiateRefund(eventOrder);
    // TODO_optional scope: try having own refund model to track and display user about refund details.
  }

  @Override
  public String getPaymentIntegratorIdForOrderId(String orderId) {
    return eventOrderRepository.findByOrderId(orderId).getPaymentIntegratorOrderId();
  }

  @Override
  public Boolean isPaymentCompleted(String orderId) {
    // TODO_optional scope: try having own payment model, and use that service to get payment completion status
    String paymentIntegratorId = getPaymentIntegratorIdForOrderId(orderId);
    return paymentIntegratorService.isPaymentCompleted(paymentIntegratorId);
  }
}
