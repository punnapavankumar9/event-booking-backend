package com.punna.eventcatalog.dto;

import com.punna.eventcatalog.model.EventDurationDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {

    @Id
    private ObjectId id;

    private String name;

    private String description;

    private String organizerId;

    // default to venue capacity
    private Integer maximumCapacity;

    private BigDecimal price;

    private ObjectId venueId;

    private EventDurationDetails eventDurationDetails;

    private Map<String, Object> additionalDetails;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

}
