package com.punna.order.model;

import com.punna.order.dto.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@Entity()
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Order {

  @NotNull
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private OrderInfo info;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @NotNull
  private String eventId;

  @NotNull
  private BigDecimal amount;

  private String eventOrderId;

  @CreatedBy
  private String createdBy;

  @CreatedDate
  private Instant createdDate;

  @NotNull
  private EventType eventType;

  @NotNull
  private OrderStatus orderStatus;
}
