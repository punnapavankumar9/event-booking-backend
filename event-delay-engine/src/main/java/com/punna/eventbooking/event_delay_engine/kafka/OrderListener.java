package com.punna.eventbooking.event_delay_engine.kafka;


import com.punna.commons.Constants;
import com.punna.commons.eventing.events.kafka.order.OrderCreatedEvent;
import com.punna.eventbooking.event_delay_engine.services.OrderDelayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderListener {

  private final OrderDelayService orderDelayService;

  @KafkaListener(groupId = "order-delay-group", topics = Constants.ORDER_CREATED_TOPIC)
  public void listen(OrderCreatedEvent orderCreatedEvent) {
    log.info("Received order: {}", orderCreatedEvent);
    orderDelayService.delayOrder(orderCreatedEvent.id());
  }
}
