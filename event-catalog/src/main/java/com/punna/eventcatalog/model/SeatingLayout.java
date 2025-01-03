package com.punna.eventcatalog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

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
