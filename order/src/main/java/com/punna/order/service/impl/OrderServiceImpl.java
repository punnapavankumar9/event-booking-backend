package com.punna.order.service.impl;

import com.punna.order.client.EventCoreFeignClient;
import com.punna.order.client.PaymentFeignClient;
import com.punna.order.dto.BookSeatRequestDto;
import com.punna.order.dto.EventOrderReqDto;
import com.punna.order.dto.EventOrderResDto;
import com.punna.order.dto.OrderReqDto;
import com.punna.order.dto.OrderResDto;
import com.punna.order.dto.OrderStatus;
import com.punna.order.mapper.OrderMapper;
import com.punna.order.model.Order;
import com.punna.order.repository.OrderRepository;
import com.punna.order.service.OrderService;
import org.punna.commons.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final EventCoreFeignClient eventCoreFeignClient;
  private final PaymentFeignClient paymentFeignClient;

  public OrderServiceImpl(OrderRepository orderRepository,
      EventCoreFeignClient eventCoreFeignClient, PaymentFeignClient paymentFeignClient) {
    this.orderRepository = orderRepository;
    this.eventCoreFeignClient = eventCoreFeignClient;
    this.paymentFeignClient = paymentFeignClient;
  }

  @Override
  public OrderResDto createOrder(OrderReqDto orderReqDto) {
    BookSeatRequestDto bookSeatRequestDto = BookSeatRequestDto.builder()
        .seats(orderReqDto.info().getSeats()).amount(orderReqDto.amount()).build();
    eventCoreFeignClient.bookTickets(orderReqDto.eventId(), bookSeatRequestDto);

    Order order = OrderMapper.toEntity(orderReqDto, null, OrderStatus.CREATED);
    order = orderRepository.save(order);

    EventOrderReqDto eventOrderReqDto = EventOrderReqDto.builder().orderId(order.getId())
        .amount(order.getAmount()).build();

    EventOrderResDto orderInPayment = paymentFeignClient.createOrderInPayment(eventOrderReqDto);

    order.setOrderStatus(OrderStatus.PENDING);
    order.setEventOrderId(orderInPayment.getId());
    order = orderRepository.save(order);
    return OrderMapper.toDto(order, orderInPayment.getRazorPayOrderId());
  }

  public Order findOrderByIdInternal(String id) {
    return orderRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Order.class.getSimpleName(), id));
  }

  @Override
  public OrderResDto findOrderById(String id) {
    Order order = findOrderByIdInternal(id);
    return OrderMapper.toDto(order);
  }

  @Override
  public void updateOrderStatus(String id, OrderStatus status) {
    Order order = findOrderByIdInternal(id);
    order.setOrderStatus(status);
    orderRepository.save(order);
  }


  @Override
  public OrderResDto cancelOrder(String id) {
    Order order = findOrderByIdInternal(id);
    order.setOrderStatus(OrderStatus.CANCELLED);
    Order savedOrder = orderRepository.save(order);
    // TODO Trigger kafka event to update in payment service.
    return OrderMapper.toDto(savedOrder);
  }

  @Override
  public OrderResDto markOrderAsSuccess(String id, String paymentId) {
    Order order = findOrderByIdInternal(id);
    order.setOrderStatus(OrderStatus.SUCCEEDED);
    // TODO Trigger Kafka event to update in payment service
    return OrderMapper.toDto(orderRepository.save(order));
  }

  @Override
  public OrderResDto markPaymentAsFailure(String id, String paymentId) {
    Order order = findOrderByIdInternal(id);
    order.setOrderStatus(OrderStatus.FAILED);
    // TODO Trigger Kafka event to update in payment service
    return OrderMapper.toDto(orderRepository.save(order));
  }
}
