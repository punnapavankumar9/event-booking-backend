package com.punna.eventcatalog.mapper;

import com.punna.eventcatalog.dto.SeatingLayoutDto;
import com.punna.eventcatalog.model.SeatingLayout;

public class SeatingLayoutMapper {

    public static SeatingLayout toSeatingLayout(SeatingLayoutDto seatingLayout) {
        return SeatingLayout
                .builder()
                .id(seatingLayout.getId())
                .seatTiers(seatingLayout.getSeatTiers())
                .capacity(seatingLayout.getCapacity())
                .build();
    }

    public static SeatingLayoutDto toSeatingLayoutDto(SeatingLayout seatingLayout) {
        return SeatingLayoutDto
                .builder()
                .capacity(seatingLayout.getCapacity())
                .seatTiers(seatingLayout.getSeatTiers())
                .id(seatingLayout.getId())
                .build();
    }

    public static void merge(SeatingLayout seatingLayout, SeatingLayoutDto seatingLayoutDto) {
        if (seatingLayoutDto.getCapacity() != null) {
            seatingLayout.setCapacity(seatingLayoutDto.getCapacity());
        }
        if (seatingLayoutDto.getSeatTiers() != null) {
            seatingLayout.setSeatTiers(seatingLayoutDto.getSeatTiers());
        }
    }
}
