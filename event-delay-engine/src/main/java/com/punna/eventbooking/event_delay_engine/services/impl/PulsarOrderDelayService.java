package com.punna.eventbooking.event_delay_engine.services.impl;

import static com.punna.commons.Constants.PULSAR_10_MIN_DELAY_TOPIC;

import com.punna.commons.eventing.events.kafka.order.OrderCreatedEvent;
import com.punna.eventbooking.event_delay_engine.services.OrderDelayService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PulsarOrderDelayService implements OrderDelayService {

  private final PulsarTemplate<OrderCreatedEvent> pulsarTemplate;

  @Override
  public void delayOrder(String orderId) {
    log.info("Delaying order: {}", orderId);
    // TODO_optional make it 10 minutes.
    pulsarTemplate.newMessage(new OrderCreatedEvent(orderId))
        .withTopic(PULSAR_10_MIN_DELAY_TOPIC)
        .withMessageCustomizer(mc -> mc.deliverAfter(60, TimeUnit.SECONDS))
        .sendAsync().thenAccept(messageId -> {
          log.info("Scheduled Delayed order: {} wit messageId {}", orderId, messageId);
        });
  }
}
