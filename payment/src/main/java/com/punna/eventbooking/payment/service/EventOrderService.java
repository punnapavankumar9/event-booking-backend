package com.punna.eventbooking.payment.service;


import com.punna.eventbooking.payment.dto.EventOrderReqDto;
import com.punna.eventbooking.payment.dto.EventOrderResDto;
import com.punna.eventbooking.payment.model.EventOrder;
import com.razorpay.RazorpayException;
import lombok.SneakyThrows;

public interface EventOrderService {

  EventOrderResDto createOrder(EventOrderReqDto eventOrderReqDto);

  void initiateRefund(EventOrder eventOrder) throws RazorpayException;

  String getPaymentIntegratorIdForOrderId(String orderId);

  Boolean isPaymentCompleted(String razorPayOrderId);
}
