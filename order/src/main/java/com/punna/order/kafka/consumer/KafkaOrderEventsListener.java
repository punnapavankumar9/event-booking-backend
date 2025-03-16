package com.punna.order.kafka.consumer;

import com.punna.commons.Constants;
import com.punna.commons.eventing.events.kafka.order.OrderTimeoutEvent;
import com.punna.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaOrderEventsListener {

  private final OrderService orderService;

  @KafkaListener(topics = Constants.ORDER_TIMEOUT_TOPIC, groupId = "order-timeout-group")
  public void listen(OrderTimeoutEvent event) {
    orderService.validatePaymentCompletion(event.id());
  }
}
