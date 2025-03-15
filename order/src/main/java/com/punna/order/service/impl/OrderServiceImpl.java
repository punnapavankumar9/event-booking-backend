package com.punna.order.service.impl;

import com.punna.commons.exception.EntityNotFoundException;
import com.punna.commons.exception.EventApplicationException;
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
import com.punna.order.service.OrderEventingService;
import com.punna.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final EventCoreFeignClient eventCoreFeignClient;
  private final PaymentFeignClient paymentFeignClient;
  private final OrderEventingService orderEventingService;


  // SAGA:choreography for compensations
  // SAGA:orchestration for booking
  @Override
  public OrderResDto createOrder(OrderReqDto orderReqDto) {
    BookSeatRequestDto bookSeatRequestDto = BookSeatRequestDto.builder()
        .seats(orderReqDto.info().getSeats()).amount(orderReqDto.amount()).build();
    try {
      eventCoreFeignClient.bookTickets(orderReqDto.eventId(), bookSeatRequestDto);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new EventApplicationException("Unable to book tickets", 500);
    }

    try {
      Order order = null;
      try {
        order = OrderMapper.toEntity(orderReqDto, null, OrderStatus.CREATED);
        order = orderRepository.save(order);

        EventOrderReqDto eventOrderReqDto = EventOrderReqDto.builder().orderId(order.getId())
            .amount(order.getAmount()).build();

        order.setOrderStatus(OrderStatus.PENDING);
        EventOrderResDto orderInPayment = null;
        try {
          orderInPayment = paymentFeignClient.createOrderInPayment(
              eventOrderReqDto);
        } catch (Exception e) {
          log.error(e.getMessage());
          throw new EventApplicationException("Unable to create payment", 500);
        }
        try {
          order.setEventOrderId(orderInPayment.getId());
          order = orderRepository.save(order);
          orderEventingService.sendOrderCreatedEvent(order.getId());
          return OrderMapper.toDto(order, orderInPayment.getRazorPayOrderId());

        } catch (Exception e) {
          // TODO (kafka) delete created payment, or mark it as failed/invalid.
          throw e;
        }
      } catch (Exception e) {
        if (order != null) {
          orderRepository.deleteById(order.getId());
        }
        log.error(e.getMessage());
        throw new EventApplicationException("Unable to create order", 500);
      }
    } catch (Exception e) {
      orderEventingService.sendUnblockTicketsEvent(orderReqDto.info().getSeats());
      log.error(e.getMessage());
      throw new EventApplicationException("Unable to create order", 500);
    }
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
    // TODO check if order can be cancellable by verify if payment is made and check if event is already started or 30min only left to start
    order.setOrderStatus(OrderStatus.CANCELLED);
    Order savedOrder = orderRepository.save(order);
    // TODO will be listened by the mail service and will send mail and payment service will initiate refund
    orderEventingService.sendOrderCanceledEvent(order);
    orderEventingService.sendUnblockTicketsEvent(order.getInfo().getSeats());
    return OrderMapper.toDto(savedOrder);
  }

  @Override
  public OrderResDto markOrderAsSuccess(String id, String paymentId) {
    Order order = findOrderByIdInternal(id);
    order.setOrderStatus(OrderStatus.SUCCEEDED);
    order = orderRepository.save(order);
    // TODO Trigger Kafka event to update in payment service and send mail to User.
    orderEventingService.sendOrderSuccessEvent(order);
    return OrderMapper.toDto(order);
  }

  @Override
  public OrderResDto markPaymentAsFailure(String id, String paymentId) {
    Order order = findOrderByIdInternal(id);
    order.setOrderStatus(OrderStatus.FAILED);
    order = orderRepository.save(order);
    // TODO Trigger Kafka event to update in payment service
    orderEventingService.sendUnblockTicketsEvent(order.getInfo().getSeats());
    orderEventingService.sendOrderFailedEvent(order.getId());
    return OrderMapper.toDto(order);
  }

  @Override
  public void validatePaymentCompletion(String id) {
    Order order = findOrderByIdInternal(id);
    if (order.getOrderStatus() == OrderStatus.PENDING) {
      order.setOrderStatus(OrderStatus.TIMEOUT);
      // TODO_optional verify with payment service.
      orderRepository.save(order);
      orderEventingService.sendUnblockTicketsEvent(order.getInfo().getSeats());
    }
  }
}
