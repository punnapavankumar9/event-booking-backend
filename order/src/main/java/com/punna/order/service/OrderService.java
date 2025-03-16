package com.punna.order.service;

import com.punna.order.dto.OrderReqDto;
import com.punna.order.dto.OrderResDto;
import com.punna.order.dto.OrderStatus;

public interface OrderService {

  OrderResDto createOrder(OrderReqDto orderReqDto);

  OrderResDto findOrderById(String id);

  void updateOrderStatus(String id, OrderStatus status);

  OrderResDto cancelOrder(String id);

  void validateAndMarkOrderSuccess(String id);

  OrderResDto markOrderAsSuccess(String id);

  OrderResDto markPaymentAsFailure(String id, String paymentId);

  void validatePaymentCompletion(String id);
}
