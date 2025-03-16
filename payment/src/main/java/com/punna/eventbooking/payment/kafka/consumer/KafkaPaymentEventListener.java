package com.punna.eventbooking.payment.kafka.consumer;

import static com.punna.commons.Constants.PAYMENT_FAILED_TOPIC;
import static com.punna.commons.Constants.PAYMENT_REFUND_TOPIC;

import com.punna.commons.eventing.events.kafka.payment.PaymentFailedEvent;
import com.punna.commons.eventing.events.kafka.payment.PaymentRefundEvent;
import com.punna.eventbooking.payment.service.EventOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPaymentEventListener {

  private final EventOrderService eventOrderService;

  @KafkaListener(topics = PAYMENT_FAILED_TOPIC, groupId = "payment-failed-group")
  public void paymentFailed(ConsumerRecord<String, PaymentFailedEvent> record) {
    log.info("Payment failed event received: {}", record);
  }

  @KafkaListener(topics = PAYMENT_REFUND_TOPIC, groupId = "payment-refund-group")
  public void paymentRefund(ConsumerRecord<String, PaymentRefundEvent> record) {
    log.info("Payment refund event received: {}", record);
  }
}
