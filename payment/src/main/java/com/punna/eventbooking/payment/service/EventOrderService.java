package com.punna.eventbooking.payment.service;


import com.punna.eventbooking.payment.dto.EventOrderReqDto;
import com.punna.eventbooking.payment.dto.EventOrderResDto;

public interface EventOrderService {

  EventOrderResDto createOrder(EventOrderReqDto eventOrderReqDto);
}
