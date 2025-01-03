package com.punna.eventcatalog.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.punna.commons.validation.groups.CreateGroup;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
// pricing and tier order details.
public class PricingTierMap {
    @NotNull(message = "name must not be null", groups = CreateGroup.class)
    private String name;

    @NotNull(message = "price must not be null", groups = CreateGroup.class)
    @Positive(message = "price must be positive")
    private BigDecimal price;

    @NotNull(message = "tier order must not be null", groups = CreateGroup.class)
    @Positive(message = "price must be positive")
    private Integer tierOrder;
}
