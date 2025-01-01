package com.punna.eventcatalog.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.punna.commons.validation.groups.CreateGroup;

import java.util.List;

@Data
@Builder
public class SeatTier {

    @NotNull(message = "tier name must not be null", groups = CreateGroup.class)
    private String name;

    private List<Seat> seats;

    // higher the order the longer the distance from screen/stage
    @NotNull(message = "tier order must not be null", groups = CreateGroup.class)
    @Positive(message = "Order must be positive")
    private Integer order;

    @NotNull(message = "rows must not be null", groups = CreateGroup.class)
    @Positive(message = "rows must be positive")
    private Integer rows;

    @NotNull(message = "columns must not be null", groups = CreateGroup.class)
    @Positive(message = "columns must be positive")
    private Integer columns;
}
