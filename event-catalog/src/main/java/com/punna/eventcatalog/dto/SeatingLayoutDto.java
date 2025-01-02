package com.punna.eventcatalog.dto;


import com.punna.eventcatalog.model.SeatTier;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.punna.commons.validation.groups.CreateGroup;
import org.punna.commons.validation.groups.UpdateGroup;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatingLayoutDto {

    @NotNull(message = "id must not be null", groups = UpdateGroup.class)
    @Null(message = "id must not be provided", groups = CreateGroup.class)
    private String id;

    @NotNull(message = "capacity must not be null", groups = CreateGroup.class)
    @Positive(message = "capacity must be positive")
    private Integer capacity;

    @Valid
    private List<SeatTier> seatTiers;
}
