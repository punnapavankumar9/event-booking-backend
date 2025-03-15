package com.punna.order.service.impl;

import static com.punna.order.config.KafkaConfig.ORDER_CANCELLED_TOPIC;
import static com.punna.order.config.KafkaConfig.ORDER_CREATED_TOPIC;
import static com.punna.order.config.KafkaConfig.ORDER_SUCCESS_TOPIC;
import static com.punna.order.config.KafkaConfig.ORDER_TIMEOUT_TOPIC;

import com.punna.order.config.KafkaConfig;
import com.punna.order.config.KafkaConfig.OrderEvents;
import com.punna.order.dto.kafka.OrderCancelledEvent;
import com.punna.order.dto.kafka.OrderCreatedEvent;
import com.punna.order.dto.kafka.OrderFailedEvent;
import com.punna.order.dto.kafka.OrderSuccessEvent;
import com.punna.order.dto.kafka.OrderTimeoutEvent;
import com.punna.order.model.Order;
import com.punna.order.service.OrderEventingService;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaOrderEventingService implements OrderEventingService {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public void sendOrderCreatedEvent(String orderId) {
    OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.builder()
        .id(orderId)
        .build();
    CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
        ORDER_CREATED_TOPIC, OrderEvents.ORDER_CREATED, orderCreatedEvent);

    future.thenAccept((result) -> {
      log.info("Order created event: {}", orderCreatedEvent);
    }).exceptionally(ex -> {
      log.error("Order created event failed", ex);
      return null;
    });
  }

  @Override
  public void sendOrderFailedEvent(String orderId) {
    OrderFailedEvent orderFailedEvent = OrderFailedEvent
        .builder()
        .id(orderId)
        .build();

    CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
        KafkaConfig.ORDER_FAILED_TOPIC, OrderEvents.ORDER_FAILED, orderFailedEvent);

    future.thenAccept((result) -> {
      log.info("Order failed event: {}", orderFailedEvent);
    }).exceptionally(ex -> {
      log.error("Order failed event failed", ex);
      return null;
    });
  }

  @Override
  public void sendOrderTimeoutEvent(String orderId) {
    OrderTimeoutEvent orderTimeoutEvent = OrderTimeoutEvent.builder().id(orderId).build();

    CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
        ORDER_TIMEOUT_TOPIC, OrderEvents.ORDER_TIMEOUT, orderTimeoutEvent);
    future.thenAccept((result) -> {
      log.info("Order timeout event: {}", orderTimeoutEvent);
    }).exceptionally(ex -> {
      log.error("Order timeout event failed", ex);
      return null;
    });
  }

  @Override
  public void sendOrderSuccessEvent(Order order) {
    OrderSuccessEvent orderSuccessEvent = OrderSuccessEvent.builder()
        .id(order.getId())
        .info(order.getInfo())
        .eventId(order.getEventId())
        .amount(order.getAmount())
        .eventType(order.getEventType())
        .eventOrderId(order.getEventOrderId())
        .createdBy(order.getCreatedBy())
        .createdDate(order.getCreatedDate())
        .build();

    CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
        ORDER_SUCCESS_TOPIC, OrderEvents.ORDER_SUCCESS, orderSuccessEvent);
    future.thenAccept((result) -> {
      log.info("Order success event: {}", orderSuccessEvent);
    }).exceptionally(ex -> {
      log.error("Order success event failed", ex);
      return null;
    });
  }

  @Override
  public void sendOrderCanceledEvent(Order order) {
    OrderCancelledEvent orderCancelledEvent = OrderCancelledEvent.builder()
        .id(order.getId())
        .info(order.getInfo())
        .eventId(order.getEventId())
        .amount(order.getAmount())
        .eventType(order.getEventType())
        .eventOrderId(order.getEventOrderId())
        .createdBy(order.getCreatedBy())
        .createdDate(order.getCreatedDate())
        .build();
    CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
        ORDER_CANCELLED_TOPIC, OrderEvents.ORDER_CANCELLED, orderCancelledEvent);

    future.thenAccept((result) -> {
      log.info("Order cancel event: {}", orderCancelledEvent);
    }).exceptionally(ex -> {
      log.error("Order cancel event failed", ex);
      return null;
    });
  }
}
