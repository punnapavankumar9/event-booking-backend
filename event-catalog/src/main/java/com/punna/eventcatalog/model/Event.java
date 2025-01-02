package com.punna.eventcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    private String id;

    private String name;

    private String description;

    @CreatedBy
    private String organizerId;

    // default to venue capacity
    private Integer maximumCapacity;

    private BigDecimal price;

    private String venueId;

    private EventDurationDetails eventDurationDetails;

    private Map<String, Object> additionalDetails;

    private boolean isOpenForBooking;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    private List<PricingTierMap> pricingTierMaps;

    private SeatState seatState;
}
