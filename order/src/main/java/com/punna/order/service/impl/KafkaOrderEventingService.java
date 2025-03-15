package com.punna.order.service.impl;

import static com.punna.commons.Constants.ORDER_CANCELLED_TOPIC;
import static com.punna.commons.Constants.ORDER_CREATED_TOPIC;
import static com.punna.commons.Constants.ORDER_FAILED_TOPIC;
import static com.punna.commons.Constants.ORDER_SUCCESS_TOPIC;

import com.punna.commons.Constants;
import com.punna.commons.Constants.OrderEvents;
import com.punna.commons.eventing.events.kafka.OrderCancelledEvent;
import com.punna.commons.eventing.events.kafka.OrderCreatedEvent;
import com.punna.commons.eventing.events.kafka.OrderFailedEvent;
import com.punna.commons.eventing.events.kafka.OrderSuccessEvent;
import com.punna.commons.eventing.events.kafka.UnblockTicketsEvent;
import com.punna.order.model.Order;
import com.punna.order.model.SeatLocation;
import com.punna.order.service.OrderEventingService;
import java.util.List;
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
        ORDER_FAILED_TOPIC, OrderEvents.ORDER_FAILED, orderFailedEvent);

    future.thenAccept((result) -> {
      log.info("Order failed event: {}", orderFailedEvent);
    }).exceptionally(ex -> {
      log.error("Order failed event failed", ex);
      return null;
    });
  }

  @Override
  public void sendOrderSuccessEvent(Order order) {
    OrderSuccessEvent orderSuccessEvent = OrderSuccessEvent.builder()
        .id(order.getId())
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

  @Override
  public void sendUnblockTicketsEvent(List<SeatLocation> seatLocations) {
    List<com.punna.commons.dto.SeatLocation> seats = seatLocations.stream().map(
        seat -> com.punna.commons.dto.SeatLocation.builder().row(seat.row()).column(seat.column())
            .build()).toList();

    UnblockTicketsEvent unblockTicketsEvent = UnblockTicketsEvent.builder()
        .seatLocations(seats)
        .build();
    CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
        Constants.UNBLOCK_TICKET_TOPIC, unblockTicketsEvent);
    future.thenAccept((result) -> {
      log.info("Unblock tickets event: {}", unblockTicketsEvent);
    }).exceptionally(ex -> {
      log.error("Unblock tickets event failed", ex);
      return null;
    });
  }
}
