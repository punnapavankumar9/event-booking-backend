package com.punna.eventbooking.payment.kafka.producer;

import com.punna.commons.Constants;
import com.punna.commons.Constants.PaymentEvents;
import com.punna.commons.eventing.events.kafka.order.OrderSuccessEvent;
import com.punna.commons.eventing.events.kafka.payment.PaymentSuccessEvent;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventProducer {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void sendPaymentSuccessEvent(String orderId) {
    CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
        Constants.PAYMENT_SUCCESS_TOPIC, PaymentEvents.PAYMENT_SUCCESS,
        new PaymentSuccessEvent(orderId));

    future.thenAccept(result -> {
      log.info("Order successful event sent: {}", orderId);
    }).exceptionally(ex -> {
      log.error("Order successful event failed: {}", orderId, ex);
      return null;
    });
  }

}
