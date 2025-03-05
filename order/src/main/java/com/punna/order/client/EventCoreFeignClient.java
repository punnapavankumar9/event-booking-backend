package com.punna.order.client;


import com.punna.order.dto.BookSeatRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "event-core-service")
public interface EventCoreFeignClient {

  @PostMapping("/api/v1/events/{eventId}/seats/book")
  void bookTickets(@PathVariable String eventId,
      @RequestBody BookSeatRequestDto bookSeatRequestDto);

}
