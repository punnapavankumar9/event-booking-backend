package com.punna.commons.eventing.events.kafka.payment;

public record PaymentSuccessEvent(
    String orderId
) {

}
