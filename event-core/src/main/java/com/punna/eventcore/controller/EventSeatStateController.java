package com.punna.eventcore.controller;

import com.punna.eventcore.dto.BookSeatRequestDto;
import com.punna.eventcore.model.SeatLocation;
import com.punna.eventcore.service.EventSeatStateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events/{eventId}/seats")
public class EventSeatStateController {

  private final EventSeatStateService eventSeatStateService;

  @PostMapping("/book")
  public Mono<Void> bookSeat(@PathVariable final String eventId,
      @Validated @RequestBody BookSeatRequestDto bookSeatRequestDto) {
    return eventSeatStateService.bookSeats(eventId, bookSeatRequestDto.seats(),
        bookSeatRequestDto.amount());
  }

  @PostMapping("/block")
  public Mono<Void> blockSeat(@PathVariable final String eventId,
      @Validated @RequestBody List<SeatLocation> seatLocations) {
    return eventSeatStateService.blockSeats(eventId, seatLocations);
  }

  @PostMapping("/unblock")
  public Mono<Void> unblockSeats(@PathVariable final String eventId,
      @Validated @RequestBody List<SeatLocation> seatLocations) {
    return eventSeatStateService.unblockSeats(eventId, seatLocations);
  }
}
