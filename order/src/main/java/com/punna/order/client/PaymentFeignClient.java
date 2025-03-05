package com.punna.order.client;

import com.punna.order.dto.EventOrderReqDto;
import com.punna.order.dto.EventOrderResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("payment-service")
public interface PaymentFeignClient {

  @PostMapping("/api/v1/event-orders")
  EventOrderResDto createOrderInPayment(EventOrderReqDto eventOrderReqDto);

}
