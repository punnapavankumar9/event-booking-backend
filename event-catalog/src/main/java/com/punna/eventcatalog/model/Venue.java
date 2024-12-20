package com.punna.eventcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "venues")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {

    @Id
    private String id;

    private String name;

    private String description;

    private Integer capacity;

    private String ownerId;

    private String location;

    private String city;

    private String country;

    private Integer pincode;

    private String state;

    private String googleMapsUrl;

}
