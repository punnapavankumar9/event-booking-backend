package com.punna.eventcatalog.controller;

import com.punna.eventcatalog.dto.SeatingLayoutDto;
import com.punna.eventcatalog.service.SeatingLayoutService;
import lombok.RequiredArgsConstructor;
import org.punna.commons.validation.groups.CreateGroup;
import org.punna.commons.validation.groups.UpdateGroup;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seating-layout")
public class SeatingLayoutController {

    private final SeatingLayoutService seatingLayoutService;

    @GetMapping("/{id}")
    public Mono<SeatingLayoutDto> getSeatingLayout(@PathVariable("id") String id) {
        return seatingLayoutService.getSeatingLayoutById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SeatingLayoutDto> createSeatingLayout(
            @Validated(CreateGroup.class) @RequestBody SeatingLayoutDto seatingLayoutDto) {
        return seatingLayoutService.createSeatingLayout(seatingLayoutDto);
    }

    @PatchMapping
    public Mono<SeatingLayoutDto> updateSeatingLayout(
            @Validated(UpdateGroup.class) @RequestBody SeatingLayoutDto seatingLayoutDto) {
        return seatingLayoutService.updateSeatingLayout(seatingLayoutDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteSeatingLayout(@PathVariable("id") String id) {
        return seatingLayoutService.deleteSeatingLayout(id);
    }

}
