package com.punna.order.kafka.consumer;

import com.punna.commons.Constants;
import com.punna.commons.eventing.events.kafka.payment.PaymentSuccessEvent;
import com.punna.order.service.OrderService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaPaymentEventListener {

  private final OrderService orderService;

  public KafkaPaymentEventListener(OrderService orderService) {
    this.orderService = orderService;
  }

  @KafkaListener(topics = Constants.PAYMENT_SUCCESS_TOPIC, groupId = "order-service-payment-success-group")
  public void listen(ConsumerRecord<String, PaymentSuccessEvent> record) {
    PaymentSuccessEvent event = record.value();
    orderService.markOrderAsSuccess(event.orderId());
  }

}
