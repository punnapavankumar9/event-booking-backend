package com.punna.eventbooking.payment.repository;

import com.punna.eventbooking.payment.model.EventOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventOrderRepository extends JpaRepository<EventOrder, String> {

}
