package com.punna.eventbooking.payment.exception;

import com.punna.commons.exception.EventApplicationException;

public class OrderCreationException extends EventApplicationException {

  public OrderCreationException() {
    super("Order creation failed", 400);
  }
}
