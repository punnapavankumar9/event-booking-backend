package com.punna.eventcore.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatLocation {

    @NotNull(message = "seat row must not be null")
    private Integer row;

    @NotNull(message = "seat column must not be null")
    private Integer column;

}
