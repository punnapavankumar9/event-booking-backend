package com.punna.eventbooking.notification.client;

import com.punna.eventbooking.notification.dto.EventInfo;
import com.punna.eventbooking.notification.dto.VenueIdAndNameProjection;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("event-core-service")
public interface EventCoreFeignClient {

  // retry twice in case of non-4xx if fails let it fail no fallback.
  @GetMapping("/api/v1/events/event-info/{eventId}")
  @CircuitBreaker(name = "eventCore")
  @Retry(name = "eventCore")
  EventInfo getEventInfo(@PathVariable("eventId") String eventId);
}
