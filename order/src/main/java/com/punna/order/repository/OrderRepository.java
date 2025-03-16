package com.punna.order.repository;

import com.punna.order.model.Order;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

  List<Order> findAllByCreatedBy(String createdBy, Pageable pageable);
}
