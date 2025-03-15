package com.punna.eventbooking.event_delay_engine.services;

public interface OrderTimeoutCheckService {

  void sendOrderConfirmationCheckEvent(String orderId);

}
