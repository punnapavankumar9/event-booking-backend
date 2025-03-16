package com.punna.eventbooking.payment.service.impl;

import com.punna.commons.exception.EventApplicationException;
import com.punna.eventbooking.payment.dto.EventOrderReqDto;
import com.punna.eventbooking.payment.model.EventOrder;
import com.punna.eventbooking.payment.repository.EventOrderRepository;
import com.punna.eventbooking.payment.service.PaymentIntegratorService;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.Refund;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RazorPayEventOrderService implements PaymentIntegratorService {

  private final EventOrderRepository eventOrderRepository;
  private final RazorpayClient razorPayClient;


  @SneakyThrows
  @PreAuthorize("isAuthenticated()")
  public String createOrder(EventOrderReqDto eventOrderReqDto) {
    JSONObject orderReq = new JSONObject();
    orderReq.put("amount", eventOrderReqDto.getAmount().multiply(BigDecimal.valueOf(100)));
    orderReq.put("currency", "INR");
    orderReq.put("receipt", eventOrderReqDto.getOrderId());
    Order razorPayOrder = razorPayClient.orders.create(orderReq);
    return razorPayOrder.get("id");
//    EventOrder order = EventOrder.builder().razorPayOrderId(razorPayOrder.get("id"))
//        .orderId(eventOrderReqDto.getOrderId()).amount(eventOrderReqDto.getAmount())
//        .orderStatus(OrderStatus.PENDING).build();
//    EventOrder savedOrder = eventOrderRepository.save(order);
//    return EventOrderMapper.toEventOrderResDto(savedOrder);
  }

  @SneakyThrows
  @Override
  public String initiateRefund(EventOrder eventOrder) {
    JSONObject refundObject = new JSONObject();
    refundObject.put("amount", eventOrder.getAmount().multiply(BigDecimal.valueOf(100)));
    refundObject.put("speed", "normal");
    Refund refund = razorPayClient.payments.refund(getPaymentId(eventOrder.getOrderId()),
        refundObject);
    log.info("refund Initiated {}", refund.toString());
    return refund.get("id");
  }

  @SneakyThrows
  @Override
  public Boolean isPaymentCompleted(String orderId) {
    Order order = razorPayClient.orders.fetch(orderId);
    Integer amount_due = order.get("amount_due");
    return amount_due.equals(0);
  }

  @SneakyThrows
  public String tryGetPaymentId(String orderId) {
    List<Payment> payments = razorPayClient.orders.fetchPayments(orderId);
    for (Payment payment : payments) {
      if (payment.get("status").equals("captured")) {
        return payment.get("id");
      }
    }
    return null;
  }

  public String getPaymentId(String orderId) {
    String paymentId = this.tryGetPaymentId(orderId);
    if (paymentId == null) {
      throw new EventApplicationException("Unable to find payment id for " + orderId);
    }
    return paymentId;
  }

}
