package com.punna.eventbooking.notification.kafka.consumer;

import com.punna.commons.Constants;
import com.punna.commons.eventing.events.kafka.order.OrderCancelledEvent;
import com.punna.commons.eventing.events.kafka.order.OrderFailedEvent;
import com.punna.commons.eventing.events.kafka.order.OrderSuccessEvent;
import com.punna.eventbooking.notification.client.EventCoreFeignClient;
import com.punna.eventbooking.notification.client.IdentityFeignClient;
import com.punna.eventbooking.notification.dto.EventInfo;
import com.punna.eventbooking.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderEventListener {


  private final EventCoreFeignClient eventCoreFeignClient;
  private final EmailService emailService;
  private final IdentityFeignClient identityFeignClient;

  @KafkaListener(topics = Constants.ORDER_SUCCESS_TOPIC, groupId = "mail-service-success-event")
  public void orderSuccessEvent(ConsumerRecord<String, OrderSuccessEvent> record) {
    OrderSuccessEvent event = record.value();
    log.info("received order success event {}", event);
    EventInfo eventInfo = eventCoreFeignClient.getEventInfo(event.eventId());
    String email = identityFeignClient.findByUsernameOrEmail(event.createdBy()).getEmail();
    emailService.sendOrderSuccessEmail(eventInfo, event, email);
  }

  @KafkaListener(topics = Constants.ORDER_CANCELLED_TOPIC, groupId = "mail-service-cancelled-event")
  public void orderCancelledEvent(ConsumerRecord<String, OrderCancelledEvent> record) {
    OrderCancelledEvent event = record.value();
    log.info("received order cancelled event {}", event);
    EventInfo eventInfo = eventCoreFeignClient.getEventInfo(event.eventId());
    String email = identityFeignClient.findByUsernameOrEmail(event.createdBy()).getEmail();
    emailService.sendOrderCancelledEmail(eventInfo, event, email);
  }

  @KafkaListener(topics = Constants.ORDER_FAILED_TOPIC, groupId = "mail-service-failure-event")
  public void orderFailedEvent(ConsumerRecord<String, OrderFailedEvent> record) {
    OrderFailedEvent event = record.value();
    log.info("received order failure event {}", event);
    EventInfo eventInfo = eventCoreFeignClient.getEventInfo(event.eventId());
    String email = identityFeignClient.findByUsernameOrEmail(event.createdBy()).getEmail();
    emailService.sendOrderFailureEmail(eventInfo, event, email);
  }
}
