package com.punna.eventcore.controller;

import com.punna.eventcore.dto.SeatingLayoutDto;
import com.punna.eventcore.service.SeatingLayoutService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.punna.commons.validation.groups.CreateGroup;
import com.punna.commons.validation.groups.UpdateGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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

  @GetMapping(value = "/tier-info/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<String>> getTierInfo(@PathVariable("id") String id) {
    return seatingLayoutService.getPricingTiers(id);
  }
}
