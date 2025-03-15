package com.punna.eventbooking.payment.controller;

import com.punna.eventbooking.payment.dto.EventOrderReqDto;
import com.punna.eventbooking.payment.dto.EventOrderResDto;
import com.punna.eventbooking.payment.service.EventOrderService;
import com.punna.commons.validation.groups.CreateGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/event-orders")
public class EventOrderController {

  private final EventOrderService eventOrderService;

  public EventOrderController(EventOrderService eventOrderService) {
    this.eventOrderService = eventOrderService;
  }

  @PostMapping
  public EventOrderResDto createOrder(
      @Validated(CreateGroup.class) @RequestBody EventOrderReqDto eventOrderReqDto) {
    return eventOrderService.createOrder(eventOrderReqDto);
  }
}
