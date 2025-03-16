package com.punna.eventbooking.payment.kafka.consumer;

import com.punna.commons.Constants;
import com.punna.commons.eventing.events.kafka.order.OrderCancelledEvent;
import com.punna.commons.eventing.events.kafka.order.OrderValidationEvent;
import com.punna.eventbooking.payment.kafka.producer.OrderEventProducer;
import com.punna.eventbooking.payment.model.EventOrder;
import com.punna.eventbooking.payment.repository.EventOrderRepository;
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
  private final PaymentIntegratorService paymentIntegratorService;
  private final EventOrderRepository eventOrderRepository;

  @KafkaListener(topics = Constants.ORDER_SUCCESS_VALIDATION_TOPIC, groupId = "payment-service-order-success-validation-group")
  public void orderSuccessValidation(ConsumerRecord<String, OrderValidationEvent> consumerRecord) {
    log.info("Order success validation event received: {}", consumerRecord.value());
    if (eventOrderService.isPaymentCompleted(consumerRecord.value().orderId())) {
      orderEventProducer.sendPaymentSuccessEvent(consumerRecord.value().orderId());
    }
    // ignore if not completed
  }

  @KafkaListener(topics = Constants.ORDER_CANCELLED_TOPIC, groupId = "payment-service-order-cancelled-group")
  public void orderCancelled(ConsumerRecord<String, OrderCancelledEvent> consumerRecord) {
    log.info("Order cancelled event received: {}", consumerRecord.value());
    if (eventOrderService.isPaymentCompleted(consumerRecord.value().id())) {
      EventOrder order = eventOrderRepository.findByOrderId(consumerRecord.value().id());
      paymentIntegratorService.initiateRefund(order);
    }
    // ignore if payment is not made.
  }
}
