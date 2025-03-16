package com.punna.eventbooking.payment.repository;

import com.punna.eventbooking.payment.model.EventOrder;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventOrderRepository extends JpaRepository<EventOrder, String> {

  EventOrder findByOrderId(@NotNull String orderId);

}
