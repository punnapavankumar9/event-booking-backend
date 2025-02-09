package com.punna.eventcore.controller;

import com.punna.eventcore.model.SeatLocation;
import com.punna.eventcore.service.EventSeatStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events/{eventId}/seats")
public class EventSeatStateController {

    private final EventSeatStateService eventSeatStateService;

    @PostMapping("/book")
    public Mono<Void> bookSeat(@PathVariable final String eventId,
                               @Validated @RequestBody List<SeatLocation> seatLocations) {
        return eventSeatStateService.bookSeats(eventId, seatLocations);
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
