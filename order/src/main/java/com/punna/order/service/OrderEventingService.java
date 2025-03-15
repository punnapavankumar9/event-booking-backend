package com.punna.order.service;


import com.punna.order.model.Order;
import com.punna.order.model.SeatLocation;
import java.util.List;

public interface OrderEventingService {

  void sendOrderCreatedEvent(String orderId);

  void sendOrderFailedEvent(String orderId);

  void sendOrderSuccessEvent(Order order);

  void sendOrderCanceledEvent(Order order);

  void sendUnblockTicketsEvent(List<SeatLocation> seatLocations);
}

