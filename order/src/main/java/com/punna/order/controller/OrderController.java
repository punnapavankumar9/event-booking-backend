package com.punna.order.controller;

import com.punna.order.dto.OrderReqDto;
import com.punna.order.dto.OrderResDto;
import com.punna.order.dto.OrderStatus;
import com.punna.order.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {


  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public OrderResDto createOrder(@RequestBody OrderReqDto orderReqDto) {
    return orderService.createOrder(orderReqDto);
  }

  @GetMapping("/{id}")
  public OrderResDto findOrder(@PathVariable String id) {
    return orderService.findOrderById(id);
  }

  @GetMapping("/cancel-order/{id}")
  public OrderResDto cancelOrder(@PathVariable String id) {
    return orderService.cancelOrder(id);
  }

  @GetMapping(value = "/payment-success/{id}", params = {"paymentId"})
  public OrderResDto markOrderAsSuccess(@PathVariable String id, OrderStatus status,
      @RequestParam String paymentId) {
    return orderService.markOrderAsSuccess(id, paymentId);
  }

  @GetMapping(value = "/payment-failed/{id}", params = {"paymentId"})
  public OrderResDto markOrderAsFailed(@PathVariable String id, @RequestParam String paymentId) {
    return orderService.markPaymentAsFailure(id, paymentId);
  }
}
