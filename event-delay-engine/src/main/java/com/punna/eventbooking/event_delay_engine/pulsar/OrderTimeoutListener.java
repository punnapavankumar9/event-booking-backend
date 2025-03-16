package com.punna.eventbooking.event_delay_engine.pulsar;

import com.punna.commons.Constants;
import com.punna.commons.eventing.events.kafka.order.OrderTimeoutEvent;
import com.punna.eventbooking.event_delay_engine.services.OrderTimeoutCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTimeoutListener {

  private final OrderTimeoutCheckService orderTimeoutCheckService;

  @PulsarListener(topics = Constants.PULSAR_10_MIN_DELAY_TOPIC, subscriptionName = "order-timeout-group", subscriptionType = SubscriptionType.Shared)
  public void listen(OrderTimeoutEvent event) {
    log.info("Received order timeout event: {}", event);
    orderTimeoutCheckService.sendOrderConfirmationCheckEvent(event.id());
  }
}
