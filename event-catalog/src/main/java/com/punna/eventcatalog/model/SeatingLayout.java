package com.punna.eventcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("seating_layout")
public class SeatingLayout {
    @Id
    private String id;

    private Integer capacity;

    private Integer rows;

    private Integer columns;

    private List<Seat> seats;
}
