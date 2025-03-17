package com.punna.order.client;

import com.punna.order.dto.EventOrderReqDto;
import com.punna.order.dto.EventOrderResDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("payment-service")
public interface PaymentFeignClient {

  @PostMapping("/api/v1/event-orders")
  @Retry(name = "payment")
  @CircuitBreaker(name = "payment")
  EventOrderResDto createOrderInPayment(EventOrderReqDto eventOrderReqDto);

}
