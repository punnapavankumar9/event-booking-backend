package com.punna.eventbooking.payment.kafka.consumer;

import com.punna.commons.Constants;
import com.punna.commons.eventing.events.kafka.order.OrderValidationEvent;
import com.punna.eventbooking.payment.kafka.producer.OrderEventProducer;
import com.punna.eventbooking.payment.service.EventOrderService;
import com.punna.eventbooking.payment.service.PaymentIntegratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderEventListener {


  private final EventOrderService eventOrderService;
  private final OrderEventProducer orderEventProducer;

  @KafkaListener(topics = Constants.ORDER_SUCCESS_VALIDATION_TOPIC, groupId = "payment-service-order-success-validation-group")
  public void orderSuccessValidation(ConsumerRecord<String, OrderValidationEvent> consumerRecord) {
    log.info("Order success validation event received: {}", consumerRecord.value());
    if (eventOrderService.isPaymentCompleted(consumerRecord.value().orderId())) {
      orderEventProducer.sendPaymentSuccessEvent(consumerRecord.value().orderId());
    }
    // ignore if not completed
  }

}
