package com.punna.eventcatalog.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.punna.commons.validation.groups.CreateGroup;
import org.punna.commons.validation.groups.UpdateGroup;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VenueDto {

    @Null(message = "VenueId is not required", groups = CreateGroup.class)
    @NotNull(message = "VenueId is must not be null", groups = UpdateGroup.class)
    private String id;

    @NotNull(message = "Venue name must not be null", groups = CreateGroup.class)
    @Size(max = 50, min = 5, message = "Venue name must be between 5 and 50 characters long")
    private String name;

    @NotNull(message = "description must not be null", groups = CreateGroup.class)
    @Size(min = 5, max = 4000, message = "Venue description must be between 5 and 4000 characters long")
    private String description;

    @NotNull(message = "Capacity must not be null", groups = CreateGroup.class)
    @Positive(message = "capacity must be greater than 0")
    private Integer capacity;

    @NotNull(message = "ownerId must not be null", groups = CreateGroup.class)
    private String ownerId;

    @NotNull(message = "location must not be null", groups = CreateGroup.class)
    @Size(min = 5, max = 100, message = "Venue location must be between 5 and 100 characters long")
    private String location;

    @NotNull(message = "city must not be null", groups = CreateGroup.class)
    @Size(min = 5, max = 50, message = "Venue city must be between 5 and 50 characters long")
    private String city;

    @NotNull(message = "country must not be null", groups = CreateGroup.class)
    @Size(min = 5, max = 50, message = "Venue country must be between 5 and 50 characters long")
    private String country;

    @NotNull(message = "Pincode must not be null", groups = CreateGroup.class)
    @Min(value = 100000, message = "pincode must be between 100000 and 999999")
    @Max(value = 999999, message = "pincode must be between 100000 and 999999")
    private Integer pincode;

    @NotNull(message = "state must not be null", groups = CreateGroup.class)
    @Size(min = 5, max = 50, message = "Venue state must be between 5 and 50 characters long")
    private String state;

    @Size(min = 5, max = 1000, message = "Venue googleMapsUrl must be between 5 and 1000 characters long")
    private String googleMapsUrl;
}