package com.punna.eventcatalog.model;

import com.punna.eventcatalog.dto.EventRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
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

    private String organizerId;

    // default to venue capacity
    private Integer maximumCapacity;

    private BigDecimal price;

    private String venueId;

    private EventDurationDetails eventDurationDetails;

    private Map<String, Object> additionalDetails;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    public void merge(EventRequestDto eventRequestDto) {

        if (eventRequestDto.getName() != null) {
            this.setName(eventRequestDto.getName());
        }
        if (eventRequestDto.getDescription() != null) {
            this.setDescription(eventRequestDto.getDescription());
        }
        if (eventRequestDto.getMaximumCapacity() != null) {
            this.setMaximumCapacity(eventRequestDto.getMaximumCapacity());
        }
        if (eventRequestDto.getPrice() != null) {
            this.setPrice(eventRequestDto.getPrice());
        }
        if (eventRequestDto.getVenueId() != null) {
            this.setVenueId(eventRequestDto.getVenueId());
        }
        if (eventRequestDto.getOrganizerId() != null) {
            this.setOrganizerId(eventRequestDto.getOrganizerId());
        }
        if (eventRequestDto.getEventDurationDetails() != null) {
            this
                    .getEventDurationDetails()
                    .merge(eventRequestDto.getEventDurationDetails());
        }
        if (eventRequestDto.getAdditionalDetails() != null) {
            if (this.additionalDetails == null) {
                this.additionalDetails = new HashMap<>();
            }
            this.additionalDetails.putAll(eventRequestDto.getAdditionalDetails());
        }
    }
}
