package com.punna.eventbooking.payment.service;

import com.punna.eventbooking.payment.dto.EventOrderReqDto;
import com.punna.eventbooking.payment.model.EventOrder;
import com.razorpay.RazorpayException;
import lombok.SneakyThrows;

public interface PaymentIntegratorService {

  String createOrder(EventOrderReqDto eventOrderReqDto);

  String initiateRefund(EventOrder eventOrder);

  Boolean isPaymentCompleted(String orderId);
}
