package com.punna.eventbooking.event_delay_engine.services.impl;

import com.punna.commons.Constants;
import com.punna.commons.eventing.events.kafka.order.OrderTimeoutEvent;
import com.punna.eventbooking.event_delay_engine.services.OrderTimeoutCheckService;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaOrderTimeoutCheckService implements OrderTimeoutCheckService {

  private final KafkaTemplate<String, OrderTimeoutEvent> kafkaTemplate;

  @Override
  public void sendOrderConfirmationCheckEvent(String orderId) {
    CompletableFuture<SendResult<String, OrderTimeoutEvent>> send = kafkaTemplate.send(
        Constants.ORDER_TIMEOUT_TOPIC, orderId, new OrderTimeoutEvent(orderId));
    send.thenAccept(result -> {
      log.info("Order confirmation check event sent with orderId: {}", orderId);
    }).exceptionally(ex -> {
      log.error("Order confirmation check event failed", ex);
      return null;
    });
  }
}
