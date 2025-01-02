package com.punna.eventcatalog.dto;

import com.punna.eventcatalog.model.PricingTierMap;
import com.punna.eventcatalog.model.SeatState;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.punna.commons.validation.groups.CreateGroup;
import org.punna.commons.validation.groups.UpdateGroup;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDto {

    @Null(message = "EventId is not required", groups = CreateGroup.class)
    @NotNull(message = "EventId must not be null", groups = UpdateGroup.class)
    private String id;

    @NotNull(message = "Event name must not be null", groups = CreateGroup.class)
    @Size(max = 50, min = 5, message = "Event name must be between 5 and 50 characters long")
    private String name;

    @NotNull(message = "description must not be null", groups = CreateGroup.class)
    @Size(min = 5, max = 4000, message = "Event description must be between 5 and 4000 characters long")
    private String description;

    private String organizerId;

    @Positive(message = "maximum capacity must be greater than 0")
    private Integer maximumCapacity;

    @NotNull(message = "price must not be null", groups = CreateGroup.class)
    @Positive(message = "price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "venueId must not be null", groups = CreateGroup.class)
    private String venueId;

    @NotNull(message = "isOpenForBooking must not be null", groups = CreateGroup.class)
    private boolean isOpenForBooking;

    @Valid
    @NotNull(message = "event duration details must not be null", groups = CreateGroup.class)
    private EventDurationDetailsDto eventDurationDetails;

    private Map<String, Object> additionalDetails;

    @Valid
    private List<PricingTierMap> pricingTierMaps;

    @Valid
    private SeatState seatState;
}
