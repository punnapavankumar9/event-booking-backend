package com.punna.eventbooking.notification.client;

import com.punna.eventbooking.notification.dto.EventInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("event-core-service")
public interface EventCoreFeignClient {

  @GetMapping("/api/v1/events/event-info/{eventId}")
  EventInfo getEventInfo(@PathVariable("eventId") String eventId);

}
