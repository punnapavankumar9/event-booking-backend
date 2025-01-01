package com.punna.eventcatalog.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.punna.commons.validation.groups.CreateGroup;

@Data
@Builder
public class Seat {
    @NotNull(message = "isSpace must not be null", groups = CreateGroup.class)
    public Boolean isSpace;

    @NotNull(message = "row must not be null", groups = CreateGroup.class)
    @Positive(message = "row must be positive")
    public Integer row;

    @NotNull(message = "column must not be null", groups = CreateGroup.class)
    @Positive(message = "column must be positive")
    public Integer column;
}
