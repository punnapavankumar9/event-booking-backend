package com.punna.eventcatalog.controller;

import com.punna.eventcatalog.dto.SeatingArrangementDto;
import com.punna.eventcatalog.service.SeatingArrangementService;
import lombok.RequiredArgsConstructor;
import org.punna.commons.validation.groups.CreateGroup;
import org.punna.commons.validation.groups.UpdateGroup;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seating-arrangement")
public class SeatingArrangementController {

    private final SeatingArrangementService seatingArrangementService;

    @GetMapping("/{id}")
    public Mono<SeatingArrangementDto> getSeatingArrangement(@PathVariable("id") String id) {
        return seatingArrangementService.getSeatingArrangementById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SeatingArrangementDto> createSeatingArrangement(
            @Validated(CreateGroup.class) @RequestBody SeatingArrangementDto seatingArrangementDto) {
        return seatingArrangementService.createSeatingArrangement(seatingArrangementDto);
    }

    @PatchMapping
    public Mono<SeatingArrangementDto> updateSeatingArrangement(
            @Validated(UpdateGroup.class) @RequestBody SeatingArrangementDto seatingArrangementDto) {
        return seatingArrangementService.updateSeatingArrangement(seatingArrangementDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteSeatingArrangement(@PathVariable("id") String id) {
        return seatingArrangementService.deleteSeatingArrangement(id);
    }

}
