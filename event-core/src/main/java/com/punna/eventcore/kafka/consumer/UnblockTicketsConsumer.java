package com.punna.eventcore.kafka.consumer;

import com.punna.commons.eventing.events.kafka.core.UnblockTicketsEvent;
import com.punna.eventcore.model.SeatLocation;
import com.punna.eventcore.service.EventSeatStateService;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.util.retry.Retry;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnblockTicketsConsumer {

  private final EventSeatStateService eventSeatStateService;
  private final KafkaReceiver<String, UnblockTicketsEvent> receiver;

  @EventListener(ApplicationStartedEvent.class)
  public Disposable startConsumer() {
    log.info("Starting Unblock tickets consumer...");
    return receiver
        .receive()
        .doOnError(error -> log.error("Error receiving event, will retry", error))
        .retryWhen(Retry.fixedDelay(5, Duration.ofMinutes(1)))
        .concatMap(this::handleEvent)
        .subscribe(record -> record.receiverOffset().acknowledge());
  }

  public Mono<ReceiverRecord<String, UnblockTicketsEvent>> handleEvent(
      ReceiverRecord<String, UnblockTicketsEvent> record) {
    log.info("Received event: key {}", record.key());
    List<SeatLocation> seats = record.value().seatLocations().stream()
        .map(seat -> SeatLocation.builder().row(seat.row()).column(seat.column()).build())
        .toList();
    return eventSeatStateService.unBookSeats(record.value().id(), seats).then(Mono.just(record));
  }
}
