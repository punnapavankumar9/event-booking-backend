package com.punna.order.service;


import com.punna.order.model.Order;

public interface OrderEventingService {

  void sendOrderCreatedEvent(String orderId);

  void sendOrderFailedEvent(String orderId);

  void sendOrderTimeoutEvent(String orderId);

  void sendOrderSuccessEvent(Order order);

  void sendOrderCanceledEvent(Order order);
}

